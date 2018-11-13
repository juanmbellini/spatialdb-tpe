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
var radius = distance / EARTH_RADIUS_IN_METERS_APROX;

var region = {
    $centerSphere: [
        center.coordinates,
        radius
    ]
};

// Query
var result = db.sube_branches.find({
   geometry: {
       $geoWithin: region
    }
});
printjson(result.toArray());
print(`Found ${result.count()} records`);
