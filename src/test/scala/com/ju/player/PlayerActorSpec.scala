package com.ju.player

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}

class PlayerActorSpec extends TestKit(ActorSystem("PlayerActorSpec")) with ImplicitSender with WordSpecLike with Matchers {
  "PlayerActor" must {
    "Once created, send a message to the Game that it's joining it" in {

    }

    "Accept a move from a sender" in {

    }
  }
}
