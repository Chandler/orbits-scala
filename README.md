#### Visualize satellites using [TLE orbital projections](https://en.wikipedia.org/wiki/Two-line_element_set)

This was [originally written in javascript](https://github.com/Chandler/orbits), this repo is the scala.js port. Time will tell if the whole scala.js thing was worth it, it was mostly fun to write. If you want to dig right into some scala.js code see [App.scala](https://github.com/Chandler/orbits-scala/blob/master/src%2Fmain%2Fscala%2Forbits%2FApp.scala)

**DEMO:** http://hipsterdatascience.com/satellites

#####resources:
TLE data comes from from https://www.space-track.org

Projection calculations are from the amazing: https://github.com/shashwatak/satellite-js

3D earth modeling inspired by: http://blog.thematicmapping.org/2013/09/creating-webgl-earth-with-threejs.html

##### Scala.js Project Structure:
scala.js pulls 3rd party javascript from two sources and compiles them into target/scala-2.11/orbits-jsdeps
* js checked into this rep: src/main/resources/*
* js downloaded from remote repositories: built.sbt

scala.js compiles scala under src/main/scala/orbits into
* optimized: target/scala-2.11/orbits-fastopt.js
* non-optmized: target/scala-2.11/orbits-opt.js

when index.html is served, it reads some static css and images from web/*
then it loads target/scala-2.11/orbits-opt.js and runs the application.
