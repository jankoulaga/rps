package com.ju

import akka.actor.ActorRef
import com.ju.rulebook.{Move, Paper, Rock, Scissors}

sealed trait Action

/**
  * This trait defines a general interface for defining mappings between characters and Actions.
  */
trait KeyMapper[T] {
  val controller: ActorRef
  val display : ActorRef
  val mappings : Map[T, Action]
  private def resolveKeyToAction(key: T): Option[Action] = mappings.get(key)
  def actionByKey(key: T) = resolveKeyToAction(key).foreach(controller ! _)
}

class SimpleMapper(val controller: ActorRef, val display: ActorRef) extends KeyMapper[Char] {
  val mappings: Map[Char, Action] = Map(
    'c' -> GameTypeSelection(CvsC),
    'h' -> GameTypeSelection(PvsC),
    'r' -> MakeMove(Rock),
    'p' -> MakeMove(Paper),
    's' -> MakeMove(Scissors)
  )
}

sealed trait GameType
case object CvsC extends GameType
case object PvsC extends GameType
case class MakeMove(move: Move) extends Action
case class GameTypeSelection(gameType: GameType) extends Action
