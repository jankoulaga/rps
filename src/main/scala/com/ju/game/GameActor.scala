package com.ju.game

import akka.actor.{ActorRef, FSM, Props, Stash}
import com.ju.game.GameActor.{GameResult, PlayerResult}
import com.ju.player.PlayerActor
import com.ju.rulebook.{Move, Result}

/**
  * Steps allowed in this Finite state machine
  */
sealed trait GameStep
private case object WaitForPlayers extends GameStep
private case object WaitForMoves extends GameStep
private case object Deliberate extends GameStep

/**
  * State object
  */
sealed trait GameState {
  def isStateReady: Boolean
}
case object Empty extends GameState {
  override def isStateReady: Boolean = false
}

case class Contender(player: ActorRef, move: Option[Move] = None){
  def withMove(move: Move) = this.copy(move = Some(move))
}
case class Contest(left: Contender, right: Option[Contender] = None) extends GameState {
  def withOpposition(contender: Contender) = this.copy(right = Some(contender))
  def withMove(player: ActorRef, move: Move) = {
    if(left.player == player) this.copy(left = left.withMove(move))
    else withOpposition(Contender(player, Some(move)))
  }
  def isStateReady = Seq(left.move, right.flatMap(_.move)).flatten.size == 2
}

//Internal messages
sealed trait InternalMessage
case object AllMovesMade extends InternalMessage
case object CheckWinner extends InternalMessage

/**
  * GameActor companion object
  */
object GameActor {
  case class PlayerResult(name: String, result: Result)
  case class GameResult(results: Set[PlayerResult])
  def props(controller: ActorRef) = Props(classOf[GameActor], controller)
}

/**
  * An actor which does the following:
  * - awaits for two players to join the game
  * - awaits for player moves
  * - processes moves and decides who won/lost
  * - notifies the players of the outcome
  * - sends the outcome back to the controller
  *
  * The approach here allows to react to player moves regardles of who joined first or who made the first move.
  * If a player has made a move before the second player joined the game, that move will be used nevertheless.
  * All the interested parties will be notified of the outcome, the players and the game controller.
  *
  * @param controller an actor reference to the controller who started the game and found players.
  */
class GameActor(controller: ActorRef) extends FSM[GameStep, GameState] with Stash {
  startWith(WaitForPlayers, Empty)
  when(WaitForPlayers) {
    case Event(PlayerActor.JoinGame, Empty) =>
      log.debug(s"Player $name joined the game...")
      stay() using Contest(Contender(sender()))
    case Event(PlayerActor.JoinGame, contest @ Contest(left, None)) =>
      val player = sender()
      val contender = Contender(player)
      log.debug(s"Player $name joined the game...")
      unstashAll()
      goto(WaitForMoves) using contest.withOpposition(contender)
    case Event(move: Move, _) =>
      log.debug(s"Received $move from $name, stashing it for later")
      stash()
      stay()
  }

  when(WaitForMoves) {
    case Event(move: Move, contest: Contest) =>
      log.debug(s"Received $move from $name and storing it to state.")
      allMovesReceivedCheck(contest.withMove(sender(), move))
    case Event(AllMovesMade, contest: Contest) =>
      self ! CheckWinner
      goto(Deliberate) using contest
  }

  when(Deliberate) {
    case Event(CheckWinner, contest: Contest) =>
      controller ! notifyWinners(contest)
      goto(WaitForPlayers) using Empty
  }

  private def name: String = sender().path.name

  private def notifyWinners(contest: Contest): GameResult = {
    val leftResult = contest.left.move.get.matchAgainst(contest.right.get.move.get)
    val rightResult = contest.right.get.move.get.matchAgainst(contest.left.move.get)
    contest.left.player ! leftResult
    contest.right.get.player ! rightResult
    GameResult(Set(PlayerResult(contest.left.player.path.name, leftResult),
      PlayerResult(contest.right.get.player.path.name, rightResult))
    )
  }
  private def allMovesReceivedCheck(state: GameState): State = {
    if (state.isStateReady) self ! AllMovesMade
    stay using state
  }
}
