# PG-Loader

Program in charge of loading/dropping data from resources into a PG database

## Build

Java 11 and maven are required:

```
$ mvn clean package
```
The command will create a ```.jar``` file located in the ```<project-root>/target``` directory.

## Usage

The program must be run with the following command

```
$ java -jar <path-to-jar>
```

The execution can be customized with the following arguments.

### Execution mode

Use the ```--ar.edu.itba.spatial_db.tpe.pg_loader``` argument to set the execution mode.
The accepted values are ```populate``` and ```clear```.
**This is a required argument**

### Database connection

Use the ```--spring.datasource.url```, ```--spring.datasource.username``` and ```--spring.datasource.password``` 
arguments to set database connection. The database must be a valid schema. Check the following section.


### Complete running example

```
$ java -jar <path-to-jar> \
    --ar.edu.itba.spatial_db.tpe.pg_loader=populate \
    --spring.datasource.url=jdbc:postgresql://localhost:5432/spatialdb-tpe
    --spring.datasource.username=spatialdb
    --spring.datasource.password=12345678
```

## Expected schema

The program expects the following minimum schema (the following tables must exists in the database)

```postgresql
CREATE TABLE sube_branches_geography (
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

CREATE TABLE cities_geography (
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    location      GEOGRAPHY(POINT, 4326),
    name          VARCHAR(128),
    department    VARCHAR(128),
    population    INT
);
```

Note that the ```postgis``` extension must be installed. This can be done with the following command:

```postgresql
CREATE EXTENSION IF NOT EXISTS postgis;
```


## Author
- [Juan Marcos Bellini](https://github.com/juanmbellini)