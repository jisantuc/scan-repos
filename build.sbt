ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.1.0"

addCommandAlias(
  "ci-test",
  "scalafmtCheckAll;undeclaredCompileDependenciesTest;unusedCompileDependenciesTest;test"
)

lazy val root = (project in file(".")).settings(
  name := "scan-repos",
  libraryDependencies ++= Seq(
    "co.fs2" %% "fs2-core" % "3.2.5",
    "co.fs2" %% "fs2-io" % "3.2.5",
    "com.47deg" %% "github4s" % "0.31.0",
    "com.monovore" %% "decline-effect" % "2.2.0",
    "com.monovore" %% "decline" % "2.2.0",
    "org.http4s" %% "http4s-blaze-client" % "0.23.10",
    "org.http4s" %% "http4s-client" % "0.23.10",
    "org.typelevel" %% "cats-core" % "2.7.0",
    "org.typelevel" %% "cats-effect-kernel" % "3.3.6",
    "org.typelevel" %% "cats-effect-std" % "3.3.6",
    "org.typelevel" %% "cats-effect" % "3.3.6",
    "org.typelevel" %% "cats-kernel" % "2.7.0"
  )
)
