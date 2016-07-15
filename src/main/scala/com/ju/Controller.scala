package com.ju

import akka.actor.{ ActorRef, FSM, Props}
import com.ju.game.GameActor
import com.ju.game.GameActor.GameResult
import com.ju.player.PlayerActor
import com.ju.rulebook._

sealed trait ControllerStep
case object Init extends ControllerStep
case object HumanPlayer extends ControllerStep
case object MakeMoves extends ControllerStep
case object AwaitForResult extends ControllerStep
case object WrapItAllUp extends ControllerStep

sealed trait ControllerState
case object Nothing extends ControllerState
case class CurrentGame(game: ActorRef) extends ControllerState

private case object ImDone

object Controller {
  def props(display: ActorRef) = Props(classOf[Controller], display)
}

/**
  * An actor which is used to control the user selection workflow, and communicate with the display actor, create a
  * game depending on the Action, create players.
  *
  * @param display the actor which will diplay messages from this controller.
  */
class Controller(display: ActorRef) extends FSM[ControllerStep, ControllerState]  {
  display ! "Choose a game type: (h) for Human Vs Computer, (c) for Computer Vs Computer"
  startWith(Init, Nothing)
  when(Init){
    case Event(GameTypeSelection(gameType), Nothing) =>
      val game = context.actorOf(GameActor.props(self))
      gameType match {
        case CvsC =>
          self ! AutoMoveMaker.getMove
          goto(MakeMoves) using CurrentGame(game)
        case PvsC =>
          display ! "Make your move: (r) for Rock, (p) for Paper, (s) for Scissors."
          goto(HumanPlayer) using CurrentGame(game)
        case _ =>
          self ! ImDone
          goto(WrapItAllUp)
      }
  }

  when(HumanPlayer) {
    case Event(moveMade@ MakeMove(move), state) =>
      display ! s"You played $move"
      self ! moveMade
      goto(MakeMoves)
  }

  when(MakeMoves) {
    case Event(move: Move, state: CurrentGame) =>
      sendOutMoves(move, state, "Computer-I", "Computer-II")
    case Event(moveMade@ MakeMove(move), state: CurrentGame)  =>
      sendOutMoves(move, state, "Computer", "Human")
  }

  when(AwaitForResult) {
    case Event(gameResult: GameResult, state: CurrentGame) =>
      val winner = gameResult.results.find(_.result == Win)
      winner.fold(display ! "It's a tie!"){player => display ! s"${player.name} won!"}
      self ! ImDone
      goto(WrapItAllUp)
  }

  when(WrapItAllUp) {
    case Event(_, _) =>
      context.system.shutdown()
      stay()
  }

  whenUnhandled{
    case Event(msg, data) =>
      stay()
  }

  private def sendOutMoves(move: Move, state: CurrentGame, p1Name: String, p2Name: String): State = {
    val computer = context.actorOf(PlayerActor.props(state.game), p1Name)
    val otherPlayer = context.actorOf(PlayerActor.props(state.game), p2Name)
    val computerMove = AutoMoveMaker.getMove
    computer ! computerMove
    otherPlayer ! move
    display ! s"${computer.path.name} played $computerMove, ${otherPlayer.path.name} played $move"
    goto(AwaitForResult)
  }

}
