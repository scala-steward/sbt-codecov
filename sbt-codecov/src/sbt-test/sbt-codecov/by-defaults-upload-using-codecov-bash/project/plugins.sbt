sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.alejandrohdezma" % "sbt-codecov" % x)
  case _       => sys.error("https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html")
}
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
