package com.ju

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("RPSActorSystem")
  val display = system.actorOf(DisplayActor.props)
  val controller = system.actorOf(Controller.props(display))
  val mapper = new SimpleMapper(controller, display)
  while(!system.isTerminated){
    mapper.actionByKey(scala.io.StdIn.readChar())
  }
}