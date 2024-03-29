resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("com.github.sbt"  % "sbt-pgp"         % "2.2.1")
addSbtPlugin("com.github.sbt"  % "sbt-release"     % "1.1.0")
addSbtPlugin("org.scalameta"   % "sbt-scalafmt"    % "2.5.2")
addSbtPlugin("org.scoverage"   % "sbt-coveralls"   % "1.3.9")
addSbtPlugin("org.scoverage"   % "sbt-scoverage"   % "2.0.9")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.1.6")
addSbtPlugin("org.xerial.sbt"  % "sbt-sonatype"    % "3.10.0")
