package construction.universe.io

import cats.effect.IO
import construction.universe.throwables.OptionWithoutContentsException

object IOUtil {

  implicit class OptionOps[T](op: Option[T]){
    def ioResult(throwableMessage: String = "Could not retrieve value of option.") =
      op.map(t => IO(t)).getOrElse(IO.raiseError(OptionWithoutContentsException(throwableMessage)))
  }

}
