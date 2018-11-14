# PostgreSQL 

## Schema

In order to generate the schema, run the ```schema.sql``` SQL script.

```
$ psql <database-name> -f schema.sql
```




## Data
In order to load data check the pg-loader module.




## Queries

It is assumed that schema is already generated and data is already loaded

### Get all branches within a point (with geography)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearTo.sql``` SQL script. The spheroid variable must be set to ```true``` or ```false``` in order to set how the distance must be calculated. This query uses the table in which geolocation data is stored as geography.

```sql
\timing
SELECT
    id                  AS "Branch id",
    ST_AsText(location) AS "Location",
    branch_type         AS "Branch type",
    score               AS "Score",
    description         AS "Description",
    is_reportable       AS "Is Reportable?",
    company_id          AS "Company id",
    city                AS "City",
    address             AS "Address",
    province            AS "Province",
    open_time           AS "Open time",
    type_value          AS "Type",
    types               AS "Types"
FROM sube_branches_geography
WHERE ST_DWithin(location, ('SRID=4326;POINT(' || :lon || ' ' || :lat || ')')::geography, :distance, :spheroid)
ORDER BY ST_Distance(location, ('SRID=4326;POINT(' || :lon || ' ' || :lat || ')')::geography, :spheroid);
```

```
$ psql <database-name>  -f nearTo.sql -v lat=-34.6101 -v lon=-58.4079 -v distance=100 -v spheroid=false
```

**Note that the distance variable is in meters.**

The result will be returned ordered by distance

### Get all branches within a point (with geometry)

In order to get all branches within a certain distance, using a given point as a center, you can use the ```nearToGeometry.sql``` SQL script. This query used the table in which geolocation data is stored as geometry.

```sql
\timing
SELECT
    id                  AS "Branch id",
    ST_AsText(location) AS "Location",
    branch_type         AS "Branch type",
    score               AS "Score",
    description         AS "Description",
    is_reportable       AS "Is Reportable?",
    company_id          AS "Company id",
    city                AS "City",
    address             AS "Address",
    province            AS "Province",
    open_time           AS "Open time",
    type_value          AS "Type",
    types               AS "Types"
FROM sube_branches_geometry
WHERE ST_DWithin(location, ST_Transform(ST_GeomFromEWKT('SRID=4326;Point(' || :lon || ' ' || :lat || ')'), 5346), :distance)
ORDER BY ST_Distance(location, ST_Transform(ST_GeomFromEWKT('SRID=4326;Point(' || :lon || ' ' || :lat || ')'), 5346));
```

```
$ psql <database-name>  -f nearToGeometry.sql -v lat=-34.6101 -v lon=-58.4079 -v distance=100
```

**Note that the distance variable is in meters.**

The result will be returned ordered by distance



### Get all branches within a distance from each city

This query will return all the branches that are within a certian distance from each city saved in the database. Is similar to the previous queries, but using as a center the saved cities points. The spheroid variable must be set to ```true``` or ```false``` in order to set how the distance must be calculated. The SQL script to be used is ```cityBranches.sql```.

```sql
\timing
SELECT
    c.name                                                                      AS "City Name",
    CASE
        WHEN count(b) = 0 THEN '[ ]'
        ELSE  jsonb_pretty(
            json_agg(
                json_build_object(
                    'description', b.description,
                    'location', ST_AsText(b.location),
                    'distance', ST_Distance(b.location, c.location, :spheroid)
             ) ORDER BY ST_Distance(b.location, c.location, :spheroid)
           )::jsonb
        )
    END                                                                         AS "Branches near",
    count(b)                                                                    AS "Amount"
FROM cities_geography AS c
    LEFT JOIN sube_branches_geography AS b
       ON ST_DWithin(b.location, c.location, :distance, :spheroid)
GROUP BY c.id
ORDER BY count(b), c.name;
```

```
$ psql <database-name>  -f cityBranches.sql -v distance=100 -v spheroid=true
```

**Note that the distance variable is in meters.**

The result will be returned ordered by city name
