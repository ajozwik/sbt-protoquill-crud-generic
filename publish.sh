git clean -x -d -f src/sbt-test
#sbt -Dquill.macro.log=false "publishLocalSigned; publishSigned; sonaUpload; sonaRelease"
sbt -Dquill.macro.log=false "++3.8;publishLocalSigned; publishSigned; sonaUpload; sonaRelease"
