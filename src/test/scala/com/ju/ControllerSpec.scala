package com.ju

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.ju.rulebook.Rock
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps

class ControllerSpec extends TestKit(ActorSystem("ControllerSpec")) with WordSpecLike with Matchers {
  val optionsMsg = "Choose a game type: (h) for Human Vs Computer, (c) for Computer Vs Computer"
  val movesMsg = "Make your move: (r) for Rock, (p) for Paper, (s) for Scissors."
  "Controller" must {
    "Display a message with possible steps on init" in {
      val display = new TestProbe(system)
      val controller = system.actorOf(Controller.props(display.ref))
      display.expectMsg(optionsMsg)
    }

    "Allow only valid game actions" in {
      val display = new TestProbe(system)
      val controller = system.actorOf(Controller.props(display.ref))
      display.expectMsg(optionsMsg)
      controller ! MakeMove(Rock)
      display.expectNoMsg()
      controller ! "I have no idea what i'm doing"
      display.expectNoMsg()
    }

    "Display possible moves after selecting HumanVsComputer game type" in {
      val display = new TestProbe(system)
      val controller = system.actorOf(Controller.props(display.ref))
      display.expectMsg(optionsMsg)
      controller ! GameTypeSelection(PvsC)
      display.expectMsg(movesMsg)
    }

    "Not change state on messages which do not represent correct flow" in {
      val display = new TestProbe(system)
      val controller = system.actorOf(Controller.props(display.ref))
      display.expectMsg(optionsMsg)
      //expect GameTypeSelection ignore everything else
      controller ! "something something"
      controller ! "something else"
      controller ! GameTypeSelection(PvsC)
      display.expectMsg(movesMsg)
      //Expect MakeMove message ignore everything else
      controller ! "lkasjd"
      controller !  GameTypeSelection(PvsC)
      controller ! MakeMove(Rock)
      display.expectMsg("You played Rock")

    }
  }

}
