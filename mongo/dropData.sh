#!/bin/bash

set -e

echo "Droping Data from MongoDB"

mongo spatialdb-tpe \
    --port=27017 \
    --host=localhost \
    --eval="db.cities.drop();db.sube_branches_2d.drop();db.sube_branches_2dsphere.drop();"

echo "Finished Data from MongoDB"
