name := "sbt-protoquill-crud-generic"

organization := "com.github.ajozwik"

ThisBuild / resolvers += Resolver.sonatypeCentralSnapshots

val targetJdk = "8"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method",
  s"-target:jvm-$targetJdk",
  "-Xsource:3"
)

ThisBuild / javacOptions ++= Seq("-Xlint:deprecation", "-Xdiags:verbose", "-source", targetJdk, "-target", targetJdk)

lazy val scala212 = "2.12.20"
lazy val scala3   = "3.8.4"
ThisBuild / crossScalaVersions := Seq(scala212, scala3)

val protoQuillVersion = "0.7.0"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.6"
val `ch.qos.logback_logback-classic`           = "ch.qos.logback"              % "logback-classic" % "1.5.34"
val `org.scalatest_scalatest`                  = "org.scalatest"              %% "scalatest"       % "3.2.20" % Test
val `org.scalacheck_scalacheck`                = "org.scalacheck"             %% "scalacheck"      % "1.19.0" % Test

ThisBuild / libraryDependencies ++= Seq(
  `ch.qos.logback_logback-classic`           % Test,
  `com.typesafe.scala-logging_scala-logging` % Test,
  `org.scalatest_scalatest`,
  `org.scalacheck_scalacheck`
)

ThisScope / sbtPlugin := true

lazy val root = (project in file("."))
  .settings((pluginCrossBuild / sbtVersion) := {
    scalaBinaryVersion.value match {
      case "2.12" => "1.12.13"
      case _      => "2.0.0"
    }
  })
  .settings(
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedBufferLog := false,
    Compile / compile / wartremoverWarnings ++= Warts.allBut(Wart.ImplicitParameter, Wart.DefaultArguments, Wart.Enumeration)
  )
  .enablePlugins(SbtPlugin)
