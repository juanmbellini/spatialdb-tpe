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

### Get all branches within a point

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearTo.js``` Mongo script. This script will calculate the result sorting by distance.

```js
// Variables
var center = {
    "type" : "Point",
    "coordinates" : [
        longitude,
        latitude
    ]
};

// Query
var result = db.sube_branches.find({ 
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

```

```
$ mongo spatialdb-tpe mongo/nearTo.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;var longitude = -58.4079;var latitude = -34.6101;"
```

**Note that the distance variable is in meters.**

If sorting is not important for you, this other script can help (```nearToUnsorted.js```).

```js
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
```

```
$ mongo spatialdb-tpe mongo/nearToUnsorted.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;var longitude = -58.4079;var latitude = -34.6101;"
```

**Note that the distance variable is in meters.**

The difference between both scripts is that the first one uses ```$nearSphere```, and the second one ```$geoWithin```.



### Get all branches within a distance from each city

This query will return all the branches that are within a certian distance from each city saved in the database. Is similar to the previous queries, but using as a center the saved cities points. The SQL script to be used is ```cityBranches.js```.

```js
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
```

```
$ mongo spatialdb-tpe mongo/cityBranches.js \
    --port=27017 \
    --host=localhost \
    --eval="var distance = 1000;"
```

**Note that the distance variable is in meters.**

The result will be returned ordered by city name
