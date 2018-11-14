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
