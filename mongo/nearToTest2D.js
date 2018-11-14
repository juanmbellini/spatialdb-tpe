// Timing
var before = Date.now();

// Variables
var center = {
    "type" : "Point",
    "coordinates" : [
        longitude,
        latitude
    ]
};

var distance = 0.00007839325191 // 500 m in radians

// Query
var result = db.sube_branches_2d.find({ 
    "geometry.coordinates": {
        $nearSphere: center.coordinates,
        $maxDistance: distance
    } 
}).count();
print(`Found ${result} records`);
print(`Time elapsed: ${Date.now() - before} ms.`)
