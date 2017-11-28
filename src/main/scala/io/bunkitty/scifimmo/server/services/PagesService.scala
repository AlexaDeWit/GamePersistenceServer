package construction.universe.server.services

import java.util.UUID

import cats.effect._
import construction.universe.db.DbUtil._
import construction.universe.io.IOUtil._
import construction.universe.server.dto.request.projects.ProjectCreateRequest._
import construction.universe.server.dto.request.pages.PageRequest
import construction.universe.server.dto.request.projects.ProjectCreateRequest
import construction.universe.server.dto.response.pages.PageDto
import construction.universe.server.dto.response.pages.PageDto._
import construction.universe.server.dto.response.projects.ProjectDto
import construction.universe.server.dto.response.projects.ProjectDto._
import construction.universe.server.model.{Page, PageTexts, Pages, Project, Projects, User}
import construction.universe.throwables.InvalidAuthTokenException
import org.http4s.AuthedService
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class PagesService(db: Database) extends Http4sDsl[IO]  {

  def userIdIO(user: User): IO[Long] = user.id.map(id => IO(id)).getOrElse(IO.raiseError(InvalidAuthTokenException("Token resulted in a user with no Id")))
  lazy val pages = TableQuery[Pages]
  lazy val pageTexts = TableQuery[PageTexts]
  def route(): AuthedService[User, IO] =
    AuthedService {
      case GET -> Root / uuid as user => for {
        userId <- user.id.ioResult()
        page <- db.runIO[Page](pages.filter(_.uUID  === uuid).filter(_.fkUserId === userId).result.head)
        pageId <- page.id.ioResult()
        pageTexts = TableQuery[PageTexts]
        pageText <- db.runIO(pageTexts.filter(_.id === pageId).result.head)
        response <- Ok(PageDto(page, pageText))
      } yield response
      case req @ PATCH -> Root / uuid as user => req.req.decode[PageRequest] { pageRequest =>
        for {
          userId <- user.id.ioResult()
          pageQuery = pages.filter(_.uUID  === uuid).filter(_.fkUserId === userId)
          page <- db.runIO[Page](pageQuery.result.head)
          pageId <- page.id.ioResult()
          pageTextId <- page.id.ioResult()
          t = for { t <- pageTexts if t.id === pageTextId } yield t.textMarkdown
          updatedText <- db.runIO(t.update(pageRequest.markdownText))
          p = for { p <- pages if p.id === pageId } yield (p.name, p.description)
          updatedPage <- db.runIO(p.update(pageRequest.name, pageRequest.description))
          response <- Ok() //Maybe return something?
        } yield response
      }
      case DELETE -> Root / uuid as user => for {
        userId <- user.id.ioResult()
        pageQuery = pages.filter(_.uUID  === uuid).filter(_.fkUserId === userId)
        page <- db.runIO(pageQuery.delete)
        response <- Ok()
      } yield response
    }

}
