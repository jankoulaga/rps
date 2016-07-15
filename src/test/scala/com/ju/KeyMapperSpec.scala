package com.ju

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps

class KeyMapperSpec extends TestKit(ActorSystem("KeyMapperSpec")) with WordSpecLike with Matchers {

  class MapperStub(actorRef: ActorRef, maps: Map[String, Action]) extends KeyMapper[String]{
    override val controller: ActorRef = actorRef
    override val mappings: Map[String, Action] = maps
  }
  "KeyMapper" must {

    "correctly map characters to actions" in {
      val probe = new TestProbe(system)
      val action = GameTypeSelection(CvsC)
      val maps = Map("something" -> action)
      val mapper = new MapperStub(probe.ref, maps)
      mapper.actionByKey("something")
      probe.expectMsg(action)

    }

    "Do not send anything on unknown key" in {
      val probe = new TestProbe(system)
      val action = GameTypeSelection(CvsC)
      val maps = Map("something" -> action)
      val mapper = new MapperStub(probe.ref, maps)
      mapper.actionByKey("something else")
      probe.expectNoMsg(2 seconds)
    }
  }

}
