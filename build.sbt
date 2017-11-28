organization := "io.bunkitty"
name := "Scifi MMO Server"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.3"
scalacOptions ++= Seq("-feature", "-language:higherKinds", "-deprecation", "-Ypartial-unification")
assemblyJarName in assembly := "ScifiMmoServer.jar"

val Http4sVersion = "0.18.0-M5"
val LogbackVersion = "1.2.3"
val circeVersion = "0.9.0-M1"

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

libraryDependencies ++= Seq(
  "org.http4s"         %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"         %% "http4s-circe"        % Http4sVersion,
  "org.http4s"         %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback"     %  "logback-classic"     % LogbackVersion,
  "io.circe"           %% "circe-parser"        % circeVersion,
  "io.circe"           %% "circe-generic"       % circeVersion,
  "com.typesafe.slick" %% "slick"               % "3.2.1",
  "de.mkammerer"       %  "argon2-jvm"          % "2.2",
  "com.typesafe.slick" %% "slick-hikaricp"      % "3.2.1",
  "com.typesafe"       %  "config"              % "1.3.1",
  "org.postgresql"     %  "postgresql"          % "42.1.4",
  "org.scalatest"      %% "scalatest"           % "3.0.4"         % "test"

)