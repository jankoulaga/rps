package com.ju.rulebook

import org.scalatest.{Matchers, WordSpecLike}

class MoveSpec extends WordSpecLike with Matchers {
  "Rock" must {
    "Beat Scissors" in {

    }
    "Draw with Rock" in {

    }
    "Lose against Paper"
  }

  "Scissors" must {
    "Lose against Rock" in {

    }
    "Draw with Scissors" in {

    }
    "Beat Paper" in {

    }
  }


  "Paper" must {
    "Lose against Scissors" in {

    }
    "Beat Rock" in {

    }
    "Draw with Paper" in {

    }
  }

}
