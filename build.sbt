enablePlugins(ScalaJSPlugin)

name := "Orbits"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases")

libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.2",
    "com.lihaoyi" %%% "scalatags" % "0.4.6"
)

libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.0"
libraryDependencies += "org.denigma" %%% "threejs-facade" % "0.0.74-0.1.6" 
libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.9"
libraryDependencies += "com.github.benhutchison" %%% "prickle" % "1.1.10"

jsDependencies += "org.webjars.npm" % "jquery" % "2.2.2" / "dist/jquery.js"
jsDependencies += "org.webjars" % "bootstrap" % "3.3.6" / "bootstrap.js" dependsOn "dist/jquery.js"
jsDependencies += ProvidedJS / "three.js"
jsDependencies += ProvidedJS / "nicescroll.min.js" dependsOn "dist/jquery.js"
jsDependencies += ProvidedJS / "select2.js"
jsDependencies += ProvidedJS / "Detector.js" dependsOn "three.js"
jsDependencies += ProvidedJS / "TrackballControls.js" dependsOn "Detector.js"
jsDependencies += ProvidedJS / "satellite.js"
jsDependencies += ProvidedJS / "date_format.js"

