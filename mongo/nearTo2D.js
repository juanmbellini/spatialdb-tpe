// Timing
var before = Date.now();

// Constants
var EARTH_RADIUS_IN_METERS_APROX = 6378.1 * 1000;

// Variables
var center = {
    "type" : "Point",
    "coordinates" : [
        longitude,
        latitude
    ]
};

// Query
var result = db.sube_branches_2d.find({ 
    "geometry.coordinates": {
        $nearSphere: center.coordinates,
        $maxDistance: distance / EARTH_RADIUS_IN_METERS_APROX
    } 
});
var arr = result.toArray();
printjson(arr);
print(`Found ${arr.length} records`);
print(`Time elapsed: ${Date.now() - before} ms.`)
