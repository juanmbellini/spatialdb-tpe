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
