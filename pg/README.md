# PostgreSQL 

## Schema

In order to generate the schema, run the ```schema.sql``` SQL script.
```
$ psql <database-name> -f schema.sql
```

## Data
In order to load data check the pg-loader module

## Queries

It is assumed that schema is already generated and data is already loaded

### Get all branches within (with spheroid)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearTo.sql``` SQL script.
```
$ psql <database-name>  -f nearTo.sql -v lat=-34.6101 -v lon=-58.4079 -v distance=100
```
Note that the distance variable is in meters.

### Get all branches within (without spheroid)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearToNoSpheroid.sql``` SQL script.
```
$ psql <database-name>  -f nearToNoSpheroid -v lat=-34.6101 -v lon=-58.4079 -v distance=100
```
Note that the distance variable is in meters.
