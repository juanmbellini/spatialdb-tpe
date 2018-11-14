CREATE EXTENSION IF NOT EXISTS postgis; -- Installs the postgis extension


CREATE TABLE IF NOT EXISTS sube_branches_geography (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    location      GEOGRAPHY(POINT, 4326),
    branch_type   VARCHAR(128),
    score         SMALLINT,
    is_reportable BOOLEAN,
    description   VARCHAR(128),
    company_id    VARCHAR(128),
    city          VARCHAR(128),
    address       VARCHAR(128),
    province      VARCHAR(128),
    open_time     VARCHAR(128),
    type_value    SMALLINT,
    types         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS  sube_branches_gix
    ON sube_branches_geography
    USING GIST (location);


CREATE TABLE IF NOT EXISTS sube_branches_geometry (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    location      GEOMETRY(POINT, 5346),
    branch_type   VARCHAR(128),
    score         SMALLINT,
    is_reportable BOOLEAN,
    description   VARCHAR(128),
    company_id    VARCHAR(128),
    city          VARCHAR(128),
    address       VARCHAR(128),
    province      VARCHAR(128),
    open_time     VARCHAR(128),
    type_value    SMALLINT,
    types         VARCHAR(32)
);

CREATE INDEX IF NOT EXISTS  sube_branches_geom_gix
    ON sube_branches_geometry
    USING GIST (location);

CREATE TABLE IF NOT EXISTS cities_geography (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    location      GEOGRAPHY(POINT, 4326),
    name          VARCHAR(128),
    department    VARCHAR(128),
    population    INT
);
CREATE INDEX IF NOT EXISTS cities_geography_gix
    ON cities_geography
    USING GIST (location);
