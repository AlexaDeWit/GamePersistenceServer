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

case class ProjectsService(db: Database) extends Http4sDsl[IO]  {

  def userIdIO(user: User): IO[Long] = user.id.map(id => IO(id)).getOrElse(IO.raiseError(InvalidAuthTokenException("Token resulted in a user with no Id")))

  def route(): AuthedService[User, IO] =
    AuthedService {
      case req @ POST -> Root as user => req.req.decode[ProjectCreateRequest] { createRequest =>
        for {
          userId <- userIdIO(user)
          project = Projects(userId, createRequest.name, createRequest.description)
          projects = TableQuery[Projects]
          projectId <- db.runIO((projects returning projects.map(_.id)) += project)
          response <- Ok(projectId.toString)
        } yield response
      }
      case GET -> Root / uuid as user => for {
        userId <- userIdIO(user)
        project <- db.runIO[Project](TableQuery[Projects].filter(_.fkUserId === userId).filter(_.uUID === uuid).result.head)
        response <- Ok(ProjectDto(project))
      } yield response
      case GET -> Root as user => for {
        userId <- userIdIO(user)
        projects <- db.runIO[Seq[Project]](TableQuery[Projects].filter(_.fkUserId === userId).result)
        response <- Ok(projects.map(p => ProjectDto(p)))
      } yield response
      case req @ POST -> Root / projectUuid / "pages" as user => req.req.decode[PageRequest] { pageCreateRequest =>
        for {
          project <- db.runIO[Project](TableQuery[Projects].filter(_.uUID  === projectUuid).result.head)
          projectId <- project.id.ioResult()
          userId <- user.id.ioResult()
          pageTexts = TableQuery[PageTexts]
          pages = TableQuery[Pages]
          pageText = PageTexts(pageCreateRequest)
          pageTextId <- db.runIO((pageTexts returning pageTexts.map(_.id)) += pageText)
          page = Pages(userId, projectId, pageTextId, pageCreateRequest)
          opPageId <- db.runIO((pages returning pages.map(_.id)) += page)
          response <- Ok()
        } yield response
      }
      case GET -> Root / projectUuid / "pages" as user => for {
        project <- db.runIO[Project](TableQuery[Projects].filter(_.uUID  === projectUuid).result.head)
        projectId <- project.id.ioResult()
        userId <- user.id.ioResult()
        pageQuery = TableQuery[Pages]
        pages <- db.runIO[Seq[Page]](pageQuery.filter(_.fkUserId === userId).filter(_.fkProjectId === projectId).result)
        response <- Ok(pages.map(p => PageDto(p)))
      } yield response
    }

}
