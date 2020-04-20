# SBT plugin for uploading Scala code coverage to Codecov 

[![][github-action-badge]][github-action] [![][maven-badge]][maven] [![][steward-badge]][steward] 

Companion plugin to [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) to automatically upload coverage reports to [Codecov](https://codecov.io/) after successfully running all test configurations.

The plugin adds the following tasks/commands/settings:

|  Key               | Type    | Description                                                                                                                                                                                                                                  | Scope       |
|--------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|
| `codecovProcess`   | Setting |                            This setting contains the process that should be executed in order to upload coverage data to [Codecov](https://codecov.io/). Defaults to `bash <(curl -s https://codecov.io/bash) -Z`.                           | `ThisBuild` |
| `codecovUpload`    | Task    |                                                                 This task uploads coverage data to [Codecov](https://codecov.io/) using the process setting `codecovProcess`.                                                                | `ThisBuild` |
| `retrieveCoverage` | Task    |                                          Task used to generate coverage report. Defaults to [sbt-scoverage's `coverageAggregate`](https://github.com/scoverage/sbt-scoverage#multi-project-reports).                                         | `Project`   |
| `testCovered`      | Command | This command runs the test task in all the configurations that enable it while recovering coverage data from them. After a successful execution, uploads the coverage data to [Codecov](https://codecov.io/) using the `codecovUpload` task. | `Project`   |

> If for any reason you still want to use `testCovered` but avoid uploading coverage to Codecov, you can pass the system property `skip.coverage` set to `true`. Example:
>
> ```bash
> sbt testCovered -Dskip.coverage=true
> ```

## Example

Given an SBT build that enables the `IntegrationTest` configuration... running `sbt testCovered` command will execute the following:

```bash
set coverageEnabled in ThisBuild := true
test
it:test 
coverageAggregate
url("https://codecov.io/bash").cat #| "bash /dev/stdin -Z" !
set coverageEnabled in ThisBuild := false
```

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-codecov" % "0.1.1")
```

> Note: [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) must be provided in the SBT build in order for this plugin to work. 

[github-action]: https://github.com/alejandrohdezma/sbt-codecov/actions
[github-action-badge]: https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Falejandrohdezma%2Fsbt-codecov%2Fbadge%3Fref%3Dmaster&style=flat

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-codecov
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-codecov/badge.svg?kill_cache=1

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=
