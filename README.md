#### Visualize satellites using [TLE orbital projections](https://en.wikipedia.org/wiki/Two-line_element_set)

This was [originally written in javascript](https://github.com/Chandler/orbits), this repo is the scala.js port. Time will tell if the whole scala.js thing was worth it, it was mostly fun to write. If you want to dig right into some scala.js code see [App.scala](https://github.com/Chandler/orbits-scala/blob/master/src%2Fmain%2Fscala%2Forbits%2FApp.scala)

If you would like to add more satellites, please open a pull request [to this file](https://github.com/Chandler/orbits-scala/blob/master/offline/config.js):

**LIVE DEMO:** http://hipsterdatascience.com/satellites

#####resources:
TLE data comes from from https://www.space-track.org

Projection calculations are from the amazing: https://github.com/shashwatak/satellite-js

3D earth modeling inspired by: http://blog.thematicmapping.org/2013/09/creating-webgl-earth-with-threejs.html

##### Scala.js Project Structure:
scala.js pulls 3rd party javascript from two sources:
* js checked into this repository: src/main/resources/*
* js downloaded from remote repositories: built.sbt

scala.js compiles scala under src/main/scala/orbits into
* optimized: target/scala-2.11/orbits-fastopt.js
* non-optmized: target/scala-2.11/orbits-opt.js

#### build steps:
```
node offline/build_web_data.js
sbt fastOptJS
cp target/scala-2.11/orbits-jsdeps.js web/generated/orbits-jsdeps.js 
cp target/scala-2.11/orbits-fastopt.js web/generated/orbits.js
cd web
python -m SimpleHTTPServer 8000
```
