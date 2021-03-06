package io.bunkitty.scifimmo.throwables

case class InvalidAuthTokenException(private val message: String = "",
                                     private val cause: Throwable = None.orNull)
  extends Exception(message, cause)