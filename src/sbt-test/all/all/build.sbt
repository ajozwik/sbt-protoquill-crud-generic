import pl.jozwik.quillgeneric.sbt._

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % readQuillMacroVersionSbt))

ThisBuild / resolvers += Resolver.sonatypeRepo("releases")

ThisBuild / resolvers += Resolver.sonatypeRepo("snapshots")


val `scalaVersion_3` = "3.2.2"

name := "quill-macro-example"

ThisBuild / scalaVersion := `scalaVersion_3`


ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",             // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked"
)

val scalaTestVersion = "3.2.15"

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.15.4" % Test

val `org.scalatestplus_scalacheck-1-15` = "org.scalatestplus" %% "scalacheck-1-15" % s"$scalaTestVersion.0" % Test

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.5"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"


val generateRepositoryPackage = s"$basePackage.repository"

val monixPackage                   = s"$basePackage.monix"
val monixRepositoryPackage         = s"$monixPackage.impl"
val generateZioRepositoryPackage = s"$monixPackage.repository"

lazy val monix = projectWithSbtPlugin("monix", file("monix"))
  .settings(
    generateZioRepositories ++= Seq(
      RepositoryDescription(
        s"$domainModelPackage.Address",
        BeanIdClass(s"$domainModelPackage.AddressId"),
        s"$generateZioRepositoryPackage.AddressRepositoryGen",
        true,
        Option(s"$monixRepositoryPackage.AddressRepositoryImpl[Dialect, Naming]"),
        None,
        Map("city" -> "city")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Cell4d",
        BeanIdClass(s"$domainModelPackage.Cell4dId", KeyType.Composite),
        s"$generateZioRepositoryPackage.Cell4dRepositoryGen",
        false,
        None,
        None,
        Map("id.fk1" -> "x", "id.fk2" -> "y", "id.fk3" -> "z", "id.fk4" -> "t")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Configuration",
        BeanIdClass(s"$domainModelPackage.ConfigurationId"),
        s"$generateZioRepositoryPackage.ConfigurationRepositoryGen",
        false,
        None,
        None,
        Map("id" -> "key")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Person",
        BeanIdClass(s"$domainModelPackage.PersonId"),
        s"$generateZioRepositoryPackage.PersonRepositoryGen",
        true,
        Option(s"$monixRepositoryPackage.PersonRepositoryImpl[Dialect, Naming]"),
        None,
        Map("birthDate" -> "dob")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Product",
        BeanIdClass(s"$domainModelPackage.ProductId"),
        s"$generateZioRepositoryPackage.ProductRepositoryGen",
        true
      ),
      RepositoryDescription(
        s"$domainModelPackage.Sale",
        BeanIdClass(s"$domainModelPackage.SaleId", KeyType.Composite),
        s"$generateZioRepositoryPackage.SaleRepositoryGen",
        false,
        None,
        None,
        Map("id.fk1" -> "PRODUCT_ID", "id.fk2" -> "PERSON_ID")
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
        `org.scalatestplus_scalacheck-1-15`,
        `com.typesafe.scala-logging_scala-logging`,
        `ch.qos.logback_logback-classic`,
        `com.h2database_h2` % Test
      ),
      Compile / doc / sources := Seq.empty
    )
