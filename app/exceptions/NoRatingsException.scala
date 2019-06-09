package exceptions

case class NoRatingsException() extends Exception("You have not rated any books yet")
