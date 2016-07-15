package com.ju

import akka.actor.{ActorRef, FSM}

sealed trait ControllerStep
sealed trait ControllerState

/**
  * An actor which is used to control the user selection workflow, and communicate with the display actor, create a
  * game depending on the Action, create players.
  * @param display the actor which will diplay messages from this controller.
  */
class Controller(display: ActorRef) extends FSM[ControllerStep, ControllerState]  {

}


sealed trait Action

case object UnknownAction

/**
  * This trait defines a general interface for defining mappings between characters and Actions.
  */
trait KeyMapper {
  val controller: ActorRef
  def resolveKeyToAction(char: Char): Option[Action]
  def keyPressed(char: Char) = resolveKeyToAction(char).fold(controller ! UnknownAction){ action => controller ! action }
}
