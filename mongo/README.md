# MongoDB 

## Data

In order to populate the Mongo database, run the ```loadData.sh``` script.

```
$ bash loadData.sh
```

**Note that this script will also create geospatial 2dsphereindexes for collections.**

In order to clear the Mongo database, run the ```dropData.sh``` script.

```
$ bash dropData.sh
```



## Queries

It is assumed that data is already loaded.

### Get all branches within a point (with 2D index)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearTo2D.js``` Mongo script. This script will calculate the result sorting by distance. Note that this query uses the collection with the 2D index.

```js
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
```

```
$ mongo spatialdb-tpe mongo/nearTo2D.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;var longitude = -58.4079;var latitude = -34.6101;"
```

**Note that the distance variable is in meters.**


### Get all branches within a point (with 2DSphere index)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearTo2DSphere.js``` Mongo script. This script will calculate the result sorting by distance. Note that this query uses the collection with the 2DSphere index.

```js
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
```

```
$ mongo spatialdb-tpe mongo/nearTo2DSphere.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;var longitude = -58.4079;var latitude = -34.6101;"
```

**Note that the distance variable is in meters.**





### Get all branches within a distance from each city

This query will return all the branches that are within a certian distance from each city saved in the database. Is similar to the previous queries, but using as a center the saved cities points. The Mongo script to be used is ```cityBranches.js```.

```js
// Timing
var before = Date.now();

// Arguments
function branch_to_useful (branch) {
	return {
		"description": branch.properties.description,
		"location": branch.geometry
	};
};

function branches_near (center) {
	return db.sube_branches.find({ 
	    geometry: {
	        $nearSphere: {
	            $geometry: center, 
	            $maxDistance: distance
	        } 
	    } 
	}).map(function (branch) {
		return {
			"description": branch.properties.description,
			"location": branch.geometry
		};
	});
}


function city_branches (city) {
	var center = city.geometry;
	var near = branches_near(center);
	return {
		"City Name": city.properties.nombre,
		"Branches Near": near,
		"Amount": near.length
	};
}

// Query
var cities = db.cities.find().sort({"properties.nombre": 1}).map(city_branches);
printjson(cities);
print(`Time elapsed: ${Date.now() - before} ms.`)
```

```
$ mongo spatialdb-tpe mongo/cityBranches.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;"
```

**Note that the distance variable is in meters.**

The result will be returned ordered by city name
