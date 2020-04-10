import scala.sys.process._

TaskKey[Unit]("checkCodecovProcess", "Checks default `codecovProcess` launches codecov bash") := {
  val expected = url("https://codecov.io/bash").cat #| "bash /dev/stdin -Z"

  assert(codecovProcess.value.toString == expected.toString)
}