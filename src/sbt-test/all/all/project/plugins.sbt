val quillMacroVersion = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -Dplugin.version=...""".stripMargin)
}

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

addSbtPlugin("com.github.ajozwik" % "sbt-protoquill-crud-generic" % quillMacroVersion)
