organization := "io.bunkitty"
name := "Scifi MMO Server"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.4"
scalacOptions ++= Seq("-feature", "-language:higherKinds", "-deprecation", "-Ypartial-unification", "-language:implicitConversions")
assemblyJarName in assembly := "ScifiMmoServer.jar"

val Http4sVersion = "0.18.2"
val LogbackVersion = "1.2.3"
val circeVersion = "0.9.1"
val doobieVersion = "0.5.1"
val jwtVersion = "0.14.1"

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

libraryDependencies ++= Seq(
  "org.http4s"            %% "http4s-blaze-server"  % Http4sVersion,
  "org.http4s"            %% "http4s-circe"         % Http4sVersion,
  "org.http4s"            %% "http4s-dsl"           % Http4sVersion,
  "io.circe"              %% "circe-core"           % circeVersion,
  "io.circe"              %% "circe-parser"         % circeVersion,
  "io.circe"              %% "circe-generic"        % circeVersion,
  "io.circe"              %% "circe-generic-extras" % circeVersion,
  "io.circe"              %% "circe-literal"        % circeVersion,
  "org.tpolecat"          %% "doobie-core"          % doobieVersion,
  "org.tpolecat"          %% "doobie-postgres"      % doobieVersion,
  "org.tpolecat"          %% "doobie-scalatest"     % doobieVersion,
  "ch.qos.logback"        %  "logback-classic"      % LogbackVersion,
  "de.mkammerer"          %  "argon2-jvm"           % "2.2",
  "com.github.pureconfig" %% "pureconfig"           % "0.9.0",
  "com.pauldijou"         %% "jwt-core"             % jwtVersion,
  "org.scalatest"         %% "scalatest"            % "3.0.4"         % "test"
)
