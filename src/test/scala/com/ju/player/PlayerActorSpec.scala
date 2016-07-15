package com.ju.player

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.ju.player.PlayerActor.JoinGame
import com.ju.rulebook.Rock
import org.scalatest.{Matchers, WordSpecLike}

class PlayerActorSpec extends TestKit(ActorSystem("PlayerActorSpec")) with ImplicitSender with WordSpecLike with Matchers {
  "PlayerActor" must {
    "Once created, send a message to the Game that it's joining it" in {
      val game = new TestProbe(system)
      val player = system.actorOf(PlayerActor.props(game.ref))
      game.expectMsg(JoinGame)
    }

    "Accept a move from a sender" in {
      val game = new TestProbe(system)
      val player = system.actorOf(PlayerActor.props(game.ref))
      game.expectMsg(JoinGame)
      player ! Rock
      game.expectMsg(Rock)
    }

    "Ignore silly messages" in {
      val game = new TestProbe(system)
      val player = system.actorOf(PlayerActor.props(game.ref))
      game.expectMsg(JoinGame)
      player ! "SheepInWolvesClothing"
      game.expectNoMsg()
    }
  }
}
