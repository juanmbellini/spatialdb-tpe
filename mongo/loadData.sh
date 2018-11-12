#!/bin/bash

set -e

DEFAULT_BRANCHES_URL="https://raw.githubusercontent.com/juanmbellini/spatialdb-tpe/master/data/branches.geojson"
DEFAULT_CITIES_URL="https://raw.githubusercontent.com/juanmbellini/spatialdb-tpe/master/data/cities.geojson"
BRANCHES_DATA_URL=${1:-$DEFAULT_BRANCHES_URL}
CITIES_DATA_URL=${2:-$DEFAULT_CITIES_URL}


echo "Loading Data to MongoDB"

echo "Loading branches from" $BRANCHES_DATA_URL
curl -s $BRANCHES_DATA_URL | mongoimport \
    --port=27017 \
    --host=localhost \
    --db=spatialdb-tpe \
    --collection=sube_branches \
    --jsonArray
echo "Finished loading branches"

echo "Loading cities from" $CITIES_DATA_URL
curl -s $CITIES_DATA_URL | mongoimport \
    --port=27017 \
    --host=localhost \
    --db=spatialdb-tpe \
    --collection=cities \
    --jsonArray
echo "Finished loading cities"

echo "Finished loading Data to MongoDB"
