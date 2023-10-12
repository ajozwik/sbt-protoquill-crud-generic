import pl.jozwik.quillgeneric.sbt._

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

val `scalaVersion_3` = "3.3.1"

name := "protoquill-example"

ThisBuild / scalaVersion := `scalaVersion_3`

ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:reflectiveCalls",
  "-Wunused:imports",
  "-Wunused:linted",
  "-Wunused:locals",
  "-Wunused:params"
)

val scalaTestVersion = "3.2.17"

val `ch.qos.logback_logback-classic`                 = "ch.qos.logback"              % "logback-classic"         % "1.2.11"
val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra"      % "cassandra-driver-extras" % "3.11.3"
val `com.h2database_h2`                              = "com.h2database"              % "h2"                      % "2.2.224"
val `com.typesafe.scala-logging_scala-logging`       = "com.typesafe.scala-logging" %% "scala-logging"           % "3.9.5"
val `org.cassandraunit_cassandra-unit`               = "org.cassandraunit"           % "cassandra-unit"          % "4.3.1.0"
val `org.scalacheck_scalacheck`                      = "org.scalacheck"             %% "scalacheck"              % "1.17.0"         % Test
val `org.scalatest_scalatest`                        = "org.scalatest"              %% "scalatest"               % scalaTestVersion % Test
val `org.scalatestplus_scalacheck`                   = "org.scalatestplus"          %% "scalacheck-1-17"         % s"$scalaTestVersion.0"
val `org.tpolecat_doobie-h2`                         = "org.tpolecat"               %% "doobie-h2"               % "1.0.0-RC4"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"

val generateRepositoryPackage = s"$basePackage.repository"

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "repository" % readQuillMacroVersionSbt))

val monadPackage                   = s"$basePackage.monad"
val monadRepositoryPackage         = s"$monadPackage.impl"
val generateMonadRepositoryPackage = s"$monadPackage.repository"

lazy val monad = projectWithSbtPlugin("monad", file("monad"))
  .settings(
    generateTryRepositories ++= repositories(monadRepositoryPackage, generateMonadRepositoryPackage)
  )

val zioPackage                   = s"$basePackage.zio"
val zioRepositoryPackage         = s"$zioPackage.impl"
val generateZioRepositoryPackage = s"$zioPackage.repository"

lazy val zio = projectWithSbtPlugin("zio", file("zio"))
  .settings(
    generateZioRepositories ++= repositories(zioRepositoryPackage, generateZioRepositoryPackage)
  )

val doobiePackage                   = s"$basePackage.doobie"
val doobieRepositoryPackage         = s"$doobiePackage.impl"
val generateDoobieRepositoryPackage = s"$doobiePackage.repository"

lazy val doobie = projectWithSbtPlugin("doobie", file("doobie"))
  .settings(
    generateDoobieRepositories ++= repositories(doobieRepositoryPackage, generateDoobieRepositoryPackage),
    libraryDependencies ++= Seq(`org.tpolecat_doobie-h2` % Test)
  )

val cassandraPackage                   = s"$basePackage.cassandra"
val cassandraModelPackage              = s"$cassandraPackage.model"
val cassandraRepositoryPackage         = s"$cassandraPackage.impl"
val generateCassandraRepositoryPackage = s"$cassandraPackage.repository"

lazy val cassandra = projectWithSbtPlugin("cassandra", file("cassandra"))
  .settings(
    generateCassandraRepositories ++=
      Seq(
        RepositoryDescription(
          s"$cassandraModelPackage.Address",
          BeanIdClass(s"$cassandraModelPackage.AddressId"),
          s"$generateCassandraRepositoryPackage.AddressRepositoryGen"
        )
      ),
    libraryDependencies ++= Seq(`org.cassandraunit_cassandra-unit` % Test, `com.datastax.cassandra_cassandra-driver-extras` % Test)
  )

def repositoriesWithoutLocalDateTime(
    implementationPackage: String,
    generatePackage: String,
    generic: String = "[Dialect, Naming, C]",
    generated: Boolean = true
) =
  Seq(
    RepositoryDescription(
      s"$domainModelPackage.Cell4d",
      BeanIdClass(s"$domainModelPackage.Cell4dId", Option(4)),
      s"$generatePackage.Cell4dRepositoryGen",
      false,
      None
    ),
    RepositoryDescription(
      s"$domainModelPackage.Configuration",
      BeanIdClass(s"$domainModelPackage.ConfigurationId"),
      s"$generatePackage.ConfigurationRepositoryGen",
      false,
      None
    ),
    RepositoryDescription(
      s"$domainModelPackage.Product",
      BeanIdClass(s"$domainModelPackage.ProductId"),
      s"$generatePackage.ProductRepositoryGen",
      generated
    )
  )

def repositories(implementationPackage: String, generatePackage: String, generic: String = "[Dialect, Naming, C]", generated: Boolean = true) =
  repositoriesWithoutLocalDateTime(implementationPackage, generatePackage, generic, generated) ++ Seq(
    RepositoryDescription(
      s"$domainModelPackage.Address",
      BeanIdClass(s"$domainModelPackage.AddressId"),
      s"$generatePackage.AddressRepositoryGen",
      generated,
      Option(s"$implementationPackage.AddressRepositoryImpl$generic")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Person",
      BeanIdClass(s"$domainModelPackage.PersonId"),
      s"$generatePackage.PersonRepositoryGen",
      generated,
      Option(s"$implementationPackage.PersonRepositoryImpl$generic")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Sale",
      BeanIdClass(s"$domainModelPackage.SaleId", Option(2)),
      s"$generatePackage.SaleRepositoryGen",
      false,
      None
    )
  )

def projectWithSbtPlugin(name: String, file: File): Project =
  projectWithName(name, file)
    .dependsOn(common, common % "test -> test")
    .settings(Test / fork := true)
    .enablePlugins(QuillRepositoryPlugin)

def projectWithName(name: String, file: File): Project =
  Project(name, file)
    .settings(
      libraryDependencies ++= Seq(
        `org.scalatest_scalatest`,
        `org.scalacheck_scalacheck`,
        `org.scalatestplus_scalacheck`,
        `com.typesafe.scala-logging_scala-logging`,
        `ch.qos.logback_logback-classic`,
        `com.h2database_h2` % Test
      ),
      Compile / doc / sources := Seq.empty
    )
