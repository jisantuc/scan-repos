ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.1.0"

lazy val root = (project in file(".")).settings(
  name := "scan-repos",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % "3.3.6",
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % "3.3.6",
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % "3.3.6",
    "com.monovore" %% "decline" % "2.2.0",
    "com.monovore" %% "decline-effect" % "2.2.0",
    "com.47deg" %% "github4s" % "0.31.0",
    "org.http4s" %% "http4s-blaze-client" % "0.23.10",
    "co.fs2" %% "fs2-io" % "3.2.5"
  )
)
