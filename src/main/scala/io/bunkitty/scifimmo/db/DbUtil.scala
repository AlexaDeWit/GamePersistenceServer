package construction.universe.db

import cats.Always
import cats.effect.IO
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

object DbUtil {

  implicit class DBOps(db: Database) {
    def runIO[R](query: DBIOAction[R, NoStream, Nothing])(implicit ec: ExecutionContext): IO[R] = IO.fromFuture(Always(db.run(query)))
  }
}
