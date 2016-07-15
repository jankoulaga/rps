package com.ju.rulebook

/**
  * A common trait for all the results
  */
sealed trait Result

case object Draw extends Result

case object Win extends Result

case object Loss extends Result

/**
  * A general trait for all the moves of the game.
  */
sealed trait Move {
  /**
    * Function which is defining an outcome of a battle between this move and the one sent as parameter
    *
    * @param move another move from an opposing player
    * @return a Result object defining an outcome of a battle between this move and the one sent as parameter
    */
  def matchAgainst(move: Move): Result
}

/**
  * The rock. The hardest move
  */
case object Rock extends Move {
  override def matchAgainst(move: Move): Result = move match {
    case Paper => Loss
    case Scissors => Win
    case _ => Draw
  }
}

/**
  * Paper. The thinnest move
  */
case object Paper extends Move {
  override def matchAgainst(move: Move): Result = move match {
    case Rock => Win
    case Scissors => Loss
    case _ => Draw
  }
}

/**
  * Scissors. The sharpest move
  */
case object Scissors extends Move {
  override def matchAgainst(move: Move): Result = move match {
    case Paper => Win
    case Rock => Loss
    case _ => Draw
  }
}
