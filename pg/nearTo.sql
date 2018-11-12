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
