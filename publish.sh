git clean -x -d -f src/sbt-test
sbt -Dquill.macro.log=false clean test scripted publishSigned sonatypeRelease
