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

// Query
var result = db.sube_branches_2dsphere.find({ 
    geometry: {
        $nearSphere: {
            $geometry: center, 
            $maxDistance: 500
        } 
    } 
}).count();
print(`Found ${result} records`);
print(`Time elapsed: ${Date.now() - before} ms.`)
