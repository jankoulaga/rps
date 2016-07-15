package com.ju.game

import akka.actor.{ActorRef, FSM, Props, Stash}
import com.ju.rulebook.Result

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

}
