resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("com.github.sbt"  % "sbt2-compat"     % "0.1.0")
addSbtPlugin("com.github.sbt"  % "sbt-pgp"         % "2.3.1")
addSbtPlugin("com.github.sbt"  % "sbt-release"     % "1.4.0")
addSbtPlugin("org.scalameta"   % "sbt-scalafmt"    % "2.5.2")
addSbtPlugin("org.scoverage"   % "sbt-coveralls"   % "1.3.9")
addSbtPlugin("org.scoverage"   % "sbt-scoverage"   % "2.0.10")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.5.8")
