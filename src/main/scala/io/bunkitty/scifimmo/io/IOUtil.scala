package io.bunkitty.scifimmo.io

import cats.effect.IO
import io.bunkitty.scifimmo.throwables.OptionWithoutContentsException

object IOUtil {

  implicit class OptionOps[T](op: Option[T]){
    def ioResult(throwableMessage: String = "Could not retrieve value of option.") =
      op.map(t => IO(t)).getOrElse(IO.raiseError(OptionWithoutContentsException(throwableMessage)))
  }

}
