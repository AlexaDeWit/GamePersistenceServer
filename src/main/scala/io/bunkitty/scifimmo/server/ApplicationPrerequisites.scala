package construction.universe.server
import cats.effect.IO
import construction.universe.server.ApplicationPrerequisites.HeaderAuthMiddleware
import construction.universe.server.model.User
import org.http4s.server.AuthMiddleware
import slick.jdbc.PostgresProfile.api._



case class ApplicationPrerequisites(db: Database, authMiddleware: HeaderAuthMiddleware) {
}

object ApplicationPrerequisites {
  type HeaderAuthMiddleware = AuthMiddleware[IO, User]
}
