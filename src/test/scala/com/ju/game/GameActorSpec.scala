package com.ju.game

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}

class GameActorSpec extends TestKit(ActorSystem("GameActorSpec")) with ImplicitSender
  with WordSpecLike with Matchers {

  "GameActor" must {
    "Wait for two players to join" in {

    }

    "Stash the moveIf a one player reacts before the other joined," in {

    }

    "Wait for moves once both players joined" in {

    }

    "Overwrite the move if sent twice by the same player" in {

    }

    "Send notifications to both users about winners & loosers" in {

    }

    "Send a message back to the controller" in {

    }
  }

}
