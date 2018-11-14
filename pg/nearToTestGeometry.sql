\timing
SELECT COUNT(*)         AS "Count"
FROM sube_branches_geometry
WHERE ST_DWithin(location, ST_Transform(ST_GeomFromEWKT('SRID=4326;Point(' || :lon || ' ' || :lat || ')'), 5346), 500);
