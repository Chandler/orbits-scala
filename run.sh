# run from root

# production 
node offline/build_web_data.js
sbt fullOptJS
cp target/scala-2.11/orbits-jsdeps.js web/generated/orbits-jsdeps.js 
cp target/scala-2.11/orbits-opt.js web/generated/orbits.js 
cd web
python -m SimpleHTTPServer 8000

# dev
node offline/build_web_data.js
sbt fastOptJS
cp target/scala-2.11/orbits-jsdeps.js web/generated/orbits-jsdeps.js 
cp target/scala-2.11/orbits-fastopt.js web/generated/orbits.js
cd web
python -m SimpleHTTPServer 8000