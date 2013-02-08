import AssemblyKeys._

name := "image-binarization"

organization := "de.vorb"

version := "0.1.0"

scalaVersion := "2.10.0"


libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.0"


assemblySettings

mainClass in assembly := Some("de.vorb.vision.binarization.GUI")
