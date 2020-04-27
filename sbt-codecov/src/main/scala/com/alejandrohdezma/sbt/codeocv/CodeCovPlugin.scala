/*
 * Copyright 2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.codeocv

import scala.sys.process._

import sbt.Keys._
import sbt._

import scoverage.ScoverageKeys._
import scoverage.ScoverageSbtPlugin

/**
 * Companion plugin to `sbt-scoverage` to automatically upload coverage reports
 * to Codecov after successfully running all test configurations.
 *
 * The plugin adds the following tasks/commands/settings:
 *
 *  - '''codecovProcess''': This setting contains the process that should be executed in
 *  order to upload coverage data to Codecov. Defaults to bash <(curl -s https://codecov.io/bash) -Z.
 *  - '''retrieveCoverage''': This task uploads coverage data to Codecov using the process
 *  setting codecovProcess.
 *  - '''codecovUpload''': Task used to generate coverage report. Defaults to sbt-scoverage's
 *  coverageAggregate. This task can be skipped if SBT is run containing the system property
 *  "skip.coverage" set to "true".
 *  - '''testCovered''': This command runs the test task with cross-building in all the configurations
 *  that enable it while recovering coverage data from them. After a successful execution, uploads the
 *  coverage data to Codecov using the codecovUpload task.
 */
object CodeCovPlugin extends AutoPlugin {

  object autoImport {

    val codecovUpload = taskKey[Unit] {
      "Uploads code coverage data to Codecov. Defaults to `bash <(curl -s https://codecov.io/bash) - Z`"
    }

    val retrieveCoverage = taskKey[Unit] {
      "Generates coverage report. Defaults to `coverageAggregate`"
    }

    val codecovProcess = settingKey[ProcessBuilder] {
      "The process that should be executed in order to upload coverage data to Codecov"
    }

  }

  import autoImport._

  override def requires: Plugins = ScoverageSbtPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    commands         += testCovered,
    retrieveCoverage := coverageAggregate.value
  )

  @SuppressWarnings(Array("scalafix:DisableSyntax.!="))
  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    codecovProcess := url("https://codecov.io/bash").cat #| "bash /dev/stdin -Z",
    codecovUpload := {
      val log = streams.value.log

      if (sys.props.get("skip.coverage").contains("true")) {
        log.warn("Coverage has being disabled by the usage of '-Dskip.coverage=true'")
      } else {
        val result = codecovProcess.value ! log

        if (result != 0) {
          sys.error("Unable to upload coverage to Codecov")
        }
      }
    }
  )

  private val testCovered = Command.command("testCovered") { state =>
    val testCommand = testConfigs(state).map("+" + _ + ":test").mkString("; ")

    val enable  = "set coverageEnabled in ThisBuild := true"
    val disable = "set coverageEnabled in ThisBuild := false"

    Command.process(s"$enable; $testCommand; +retrieveCoverage; codecovUpload; $disable", state)
  }

  /**
   * Returns all the configurations on which the `test` task is enabled.
   */
  private def testConfigs(state: State): Seq[String] = {
    Project
      .extract(state)
      .currentProject
      .settings
      .map(_.key)
      .filter(_.key.label.contentEquals("test"))
      .flatMap(_.scope.config.toOption.map(_.name).toList)
  }

}
