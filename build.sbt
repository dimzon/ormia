name          := "ormia"

organization  := "hr.etna"

version       := "1.0"

scalaVersion  := "2.10.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "junit"               %   "junit"             % "4.11",
  "com.h2database"      %   "h2"                % "1.3.171"
)