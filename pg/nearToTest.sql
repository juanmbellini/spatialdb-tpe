\timing
SELECT COUNT(*)         AS "Count"
FROM sube_branches_geography
WHERE ST_DWithin(location, ('SRID=4326;POINT(' || :lon || ' ' || :lat || ')')::geography, 500, :spheroid);
