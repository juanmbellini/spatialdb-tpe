\timing
SELECT
    c.name                                                                      AS "City Name",
    CASE
        WHEN count(b) = 0 THEN '[ ]'
        ELSE jsonb_pretty(
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
