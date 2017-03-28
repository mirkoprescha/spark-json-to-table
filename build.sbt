

name := "spark-json-to-table"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided"
)

parallelExecution in test := false


