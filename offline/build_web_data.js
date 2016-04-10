/*
 * This is an offline script to generate the JSON data that powers the web app
 * This script takes the satellites defined in config.js, looks for corresponding
 * TLEs in the master TLE file, and validates that the TLEs can be parsed by satellites.js
 * 
 * This should be run everytime tle.txt is updated to keep the TLEs up to date
 *
 */
var fs = require('fs')
var satelliteConfigs = require('./config.js').configs
var satelliteJS = require('../src/main/resources/satellite.js')

var masterTLEMap = {}
var masterTlePath =  __dirname + '/tle.txt'

var tleLines = fs.readFileSync(masterTlePath).toString().split("\n")

// loop through lines 2 at a time
// a TLE looks like this:
// 1  5091U 71015S   16084.54816971  .00002974  00000-0  76844-3 0  9993
// 2  5091  65.7171   7.6582 0572500  15.9121 345.9598 13.81860961184919
for(var i = 0; i < tleLines.length; i = i + 2) {
    line1 = tleLines[i]
    line2 = tleLines[i + 1]
    if (line1 && line2) {
        line1Id = line1.split(" ")[1].slice(0,-1)
        line2Id = line2.split(" ")[1]

        if (line1Id == line2Id) {
            masterTLEMap[parseInt(line1Id)] = {line1: line1, line2: line2}
        }
    }
}

tagToIds = {}
tles = []

satelliteConfigs.forEach(config => {
    var ids = config["sat_ids"]
    var tags = config["tags"]

    var validatedIds = []

    ids.forEach(id => {
        if (id in masterTLEMap) {
            var tle = masterTLEMap[id]
            // validate that the TLE can be parsed by satellites.js
            var satrec = satelliteJS.satellite.twoline2satrec(tle["line1"], tle["line2"])
            var now = new Date()
            var positionAndVelocity = satelliteJS.satellite.propagate(
                    satrec,
                    now.getUTCFullYear(),
                    now.getUTCMonth() + 1,
                    now.getUTCDate(),
                    now.getUTCHours(),
                    now.getUTCMinutes(),
                    now.getUTCSeconds()
                );

            if (positionAndVelocity.position && positionAndVelocity.velocity) {
                tles.push({
                    sat_id: id,
                    name: config["name"],
                    line1: tle.line1,
                    line2: tle.line2
                })
                validatedIds.push(id)
            } else {
                console.log(`TLE for id: ${id} was found but not valid. satellites.js error code: ${satrec.error}`)
            }
        } else {
            console.log(`id: ${id} is being skipped because it was not in the master TLE list at ${masterTlePath}`)
        }
    })

    if (validatedIds.length > 0) {
        // for every tag in this constellation, add
        // new sat ids to the tag's id list if the sat ids were
        // validated
        tags.forEach(tag => {
            if (!(tag in tagToIds)) tagToIds[tag] = []

            var existing_ids = tagToIds[tag]

            tagToIds[tag] = existing_ids.concat(validatedIds)
        })
    }
})

fs.writeFileSync(
    'web/generated/tles_and_tags.json',
    JSON.stringify(
        {
            tles: tles,
            tags: tagToIds
        }
    ),
    'utf-8'
)