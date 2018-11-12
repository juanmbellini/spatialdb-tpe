\timing
SELECT
    c.name                                              AS "City Name",
    jsonb_pretty(json_agg(json_build_object(
        'description', b.description,
        'location', ST_AsText(b.location)
    ))::jsonb)                                          AS "Branches near"
FROM cities_geography as c, sube_branches_geography as b
WHERE ST_DWithin(b.location, c.location, :distance, :spheroid)
GROUP BY c.id, c.name
ORDER BY c.name;