package com.ju

import akka.actor.{Actor, Props}


object DisplayActor {
  def props = Props[DisplayActor]
}

class DisplayActor extends Actor {
  override def receive: Receive = {
    case msg : String => println(msg)
  }
}
