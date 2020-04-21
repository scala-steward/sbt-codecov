ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; scripted")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")

skip in publish := true

lazy val `sbt-scoverage` =
  "org.scoverage" % "sbt-scoverage" % "1.6.0" % Provided // scala-steward:off

lazy val docs = project
  .in(file("sbt-codecov-docs"))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))

lazy val `sbt-codecov` = project
  .enablePlugins(SbtPlugin)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(addSbtPlugin(`sbt-scoverage`))
