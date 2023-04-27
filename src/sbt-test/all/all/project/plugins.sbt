val quillMacroVersion = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("com.github.ajozwik" % "sbt-quill-crud-generic" % quillMacroVersion)
