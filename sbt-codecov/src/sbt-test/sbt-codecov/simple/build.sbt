import scala.sys.process._

ThisBuild / scalaVersion       := "2.13.2"
ThisBuild / crossScalaVersions := Seq("2.12.11", "2.13.2")
ThisBuild / codecovProcess     := "mv target/scala-2.12/scoverage-report/scoverage.xml scoverage.xml"

val specs2 = "org.specs2" %% "specs2-core" % "4.9.2" % Test

lazy val `module-a` = project.settings(libraryDependencies += specs2)
lazy val `module-b` = project.settings(libraryDependencies += specs2)
