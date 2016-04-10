// Tags
var GEO_SATIONARY_ORBIT = "Geostationary Orbits"
var GPS                 = "GPS"
var PLANET_LABS         = "Planet Labs"
var CUBESAT             = "Cubesats"
var IMAGING             = "Imaging Satellites"
var SUN_SYNC_ORBIT      = "Sun Sync Orbits"
var SPIRE               = "Spire"
var NASA                = "NASA"
var SPACE_STATION       = "Space Stations"
var MILITARY            = "Military"

// Each item in this list represents either a constellation of satellites or a single
// satellite if it is not a member of a constellation. The term constellation here is 
// a little bit vauge, in some cases I am splitting up satellites that are in the same constellation
// but different..sub constellations? Like Planet Labs Flock 1 vs Flock 2, which are in different orbits.
var satelliteConfigs = [
    { 
      name: "Global Positioning System",
      url: "https://en.wikipedia.org/wiki/List_of_GPS_satellites",
      sat_ids: [
        28874,
        24876,
        25030,
        25933,
        26360,
        26407,
        26605,
        26690,
        27663,
        28129,
        28190,
        28361,
        28474,
        29486,
        29601,
        32260,
        32384,
        32711,
        34661,
        35752,
        36585,
        37753,
        39166,
        39533,
        39741,
        40105,
        40534,
        40730,
        41019
      ],
      tags: [GPS]
    },
    {
       name: "Planet Labs Flock 1C",
       url: "http://spaceflight101.com/flock/",
       sat_ids: [40027, 40029, 40041, 40031, 40038, 40026, 40040, 40035, 40035, 40023, 40033],
       tags: [PLANET_LABS, CUBESAT, IMAGING, SUN_SYNC_ORBIT]
    },
    {
       name: "Planet Labs Flock 1E",
       url: "http://spaceflight101.com/flock/",
       sat_ids: [40722, 40723, 40724, 40725, 40726, 40727, 40728, 40729, 40737, 40738, 40739, 40740, 40741],
       tags: [PLANET_LABS, CUBESAT, IMAGING]
    },
    {
       name: "Spire Lemur 2",
       url: "http://space.skyrocket.de/doc_sdat/lemur-1.htm http://space.skyrocket.de/doc_sdat/lemur-2.htm",
       sat_ids: [40933, 40932, 40935, 40934],
       tags: [SPIRE, CUBESAT]
    },
    {
       name: "Hubble",
       url: "https://en.wikipedia.org/wiki/Hubble_Space_Telescope",
       sat_ids: [20580],
       tags: [NASA, IMAGING]
    },
    {
       name: "ISS",
       url: "https://en.wikipedia.org/wiki/International_Space_Station",
       sat_ids: [25544],
       tags: [NASA]
    }
]

module.exports = {
  configs: satelliteConfigs
}

