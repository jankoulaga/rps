package com.ju.player

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.ju.rulebook.{Move, Result}

/**
  * PlayerActor companion object
  */
object PlayerActor {

  sealed trait PlayerType
  case object Human extends PlayerType
  case object Computer extends PlayerType
  case object JoinGame

  def props(game: ActorRef) = Props(classOf[PlayerActor], game)
}

/**
  * The Actor acting as a Player. Once the player has been created, it is passed a game reference.
  * The player immediately sends a message to the GameActor to tell him that this player is joining the game.
  *
  * @param game the actor reference to the game itself, to which this player is joining.
  */
class PlayerActor(game: ActorRef) extends Actor with ActorLogging {
  game ! PlayerActor.JoinGame

  override def receive: Receive = {
    case move: Move =>
      log.debug(s"${self.path.name} is sending $move")
      game ! move
    case result: Result =>
      log.debug(s"${self.path.name} received $result")
  }
}