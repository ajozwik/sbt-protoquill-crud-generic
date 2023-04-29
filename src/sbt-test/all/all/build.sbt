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

val `scalaVersion_3` = "3.2.2"

name := "protoquill-example"

ThisBuild / scalaVersion := `scalaVersion_3`

ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:reflectiveCalls"
)

val scalaTestVersion = "3.2.15"

val `ch.qos.logback_logback-classic`           = "ch.qos.logback"              % "logback-classic" % "1.2.11"
val `com.h2database_h2`                        = "com.h2database"              % "h2"              % "2.1.214"
val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5"
val `org.scalacheck_scalacheck`                = "org.scalacheck"             %% "scalacheck"      % "1.17.0"         % Test
val `org.scalatest_scalatest`                  = "org.scalatest"              %% "scalatest"       % scalaTestVersion % Test
val `org.scalatestplus_scalacheck`             = "org.scalatestplus"          %% "scalacheck-1-17" % s"$scalaTestVersion.0"

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
    generateTryRepositories ++= Seq(
      RepositoryDescription(
        s"$domainModelPackage.Address",
        BeanIdClass(s"$domainModelPackage.AddressId"),
        s"$generateMonadRepositoryPackage.AddressRepositoryGen",
        true,
        Option(s"$monadRepositoryPackage.AddressRepositoryImpl[Dialect, Naming, C]"),
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Cell4d",
        BeanIdClass(s"$domainModelPackage.Cell4dId", Option(4)),
        s"$generateMonadRepositoryPackage.Cell4dRepositoryGen",
        false,
        None,
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Configuration",
        BeanIdClass(s"$domainModelPackage.ConfigurationId"),
        s"$generateMonadRepositoryPackage.ConfigurationRepositoryGen",
        false,
        None,
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Person",
        BeanIdClass(s"$domainModelPackage.PersonId"),
        s"$generateMonadRepositoryPackage.PersonRepositoryGen",
        true,
        Option(s"$monadRepositoryPackage.PersonRepositoryImpl[Dialect, Naming, C]"),
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Product",
        BeanIdClass(s"$domainModelPackage.ProductId"),
        s"$generateMonadRepositoryPackage.ProductRepositoryGen",
        true
      ),
      RepositoryDescription(
        s"$domainModelPackage.Sale",
        BeanIdClass(s"$domainModelPackage.SaleId", Option(2)),
        s"$generateMonadRepositoryPackage.SaleRepositoryGen",
        false,
        None,
        None
      )
    )
  )

val zioPackage                   = s"$basePackage.zio"
val zioRepositoryPackage         = s"$zioPackage.impl"
val generateZioRepositoryPackage = s"$zioPackage.repository"

lazy val zio = projectWithSbtPlugin("zio", file("zio"))
  .settings(
    generateZioRepositories ++= Seq(
      RepositoryDescription(
        s"$domainModelPackage.Address",
        BeanIdClass(s"$domainModelPackage.AddressId"),
        s"$generateZioRepositoryPackage.AddressRepositoryGen",
        true,
        Option(s"$zioRepositoryPackage.AddressRepositoryImpl[Dialect, Naming, C]"),
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Cell4d",
        BeanIdClass(s"$domainModelPackage.Cell4dId", Option(4)),
        s"$generateZioRepositoryPackage.Cell4dRepositoryGen",
        false,
        None,
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Configuration",
        BeanIdClass(s"$domainModelPackage.ConfigurationId"),
        s"$generateZioRepositoryPackage.ConfigurationRepositoryGen",
        false,
        None,
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Person",
        BeanIdClass(s"$domainModelPackage.PersonId"),
        s"$generateZioRepositoryPackage.PersonRepositoryGen",
        true,
        Option(s"$zioRepositoryPackage.PersonRepositoryImpl[Dialect, Naming, C]"),
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Product",
        BeanIdClass(s"$domainModelPackage.ProductId"),
        s"$generateZioRepositoryPackage.ProductRepositoryGen",
        true
      ),
      RepositoryDescription(
        s"$domainModelPackage.Sale",
        BeanIdClass(s"$domainModelPackage.SaleId", Option(2)),
        s"$generateZioRepositoryPackage.SaleRepositoryGen",
        false,
        None,
        None
      )
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
