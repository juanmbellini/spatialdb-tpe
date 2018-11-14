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
            $maxDistance: distance
        }
    }
})
var arr = result.toArray();
printjson(arr);
print(`Found ${arr.length} records`);
print(`Time elapsed: ${Date.now() - before} ms.`)
