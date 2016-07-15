package com.ju.rulebook

import org.scalatest.{Matchers, WordSpecLike}

class MoveSpec extends WordSpecLike with Matchers {
  "Rock" must {
    "Beat Scissors" in {
      Rock.matchAgainst(Scissors) shouldEqual Win
    }
    "Draw with Rock" in {
      Rock.matchAgainst(Rock) shouldEqual Draw
    }
    "Lose against Paper" in {
      Rock.matchAgainst(Paper) shouldEqual Loss
    }
  }

  "Scissors" must {
    "Lose against Rock" in {
      Scissors.matchAgainst(Rock) shouldEqual Loss
    }
    "Draw with Scissors" in {
      Scissors.matchAgainst(Scissors) shouldEqual Draw
    }
    "Beat Paper" in {
      Scissors.matchAgainst(Paper) shouldEqual Win
    }
  }


  "Paper" must {
    "Lose against Scissors" in {
      Paper.matchAgainst(Scissors) shouldEqual Loss
    }
    "Beat Rock" in {
      Paper.matchAgainst(Rock) shouldEqual Win
    }
    "Draw with Paper" in {
      Paper.matchAgainst(Paper) shouldEqual Draw
    }
  }

}
