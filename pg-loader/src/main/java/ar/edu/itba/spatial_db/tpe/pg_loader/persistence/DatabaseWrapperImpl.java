package ar.edu.itba.spatial_db.tpe.pg_loader.persistence;

import ar.edu.itba.spatial_db.tpe.pg_loader.util.CitiesGeoJsonDataReader;
import ar.edu.itba.spatial_db.tpe.pg_loader.util.DataReader;
import ar.edu.itba.spatial_db.tpe.pg_loader.util.SubeBranchTsvDataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link DatabaseWrapper}.
 */
@Component
public class DatabaseWrapperImpl implements DatabaseWrapper {

    /**
     * A {@link DataReader} used for reading data to be populated into the database.
     */
    private final DataReader<CitiesGeoJsonDataReader.CitiesGeoJsonDto> citiesGeoJsonDataReader;

    /**
     * A {@link DataReader} used for reading data to be populated into the database.
     */
    private final DataReader<SubeBranchTsvDataReader.SubeBranchDto> subeBranchTsvDataReader;

    /**
     * The {@link JdbcTemplate} used to communicate with the postgres database.
     */
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseWrapperImpl(
            final DataReader<CitiesGeoJsonDataReader.CitiesGeoJsonDto> citiesGeoJsonDataReader,
            final DataReader<SubeBranchTsvDataReader.SubeBranchDto> subeBranchTsvDataReader,
            final JdbcTemplate jdbcTemplate) {
        this.citiesGeoJsonDataReader = citiesGeoJsonDataReader;
        this.subeBranchTsvDataReader = subeBranchTsvDataReader;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Transactional
    public void populateDatabase() {
        // Populate cities
        try (final var iterator = citiesGeoJsonDataReader.readData()) {
            if (iterator.hasNext()) {
                iterator.next().getFeatures().forEach(this::addCitiesToDatabase);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        // Populate branches
        try (final var iterator = subeBranchTsvDataReader.readData()) {
            iterator.forEachRemaining(this::addBranchToDatabase);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    @Transactional
    public void dropData() {
        jdbcTemplate.update("DELETE FROM sube_branches_geography");
        jdbcTemplate.update("DELETE FROM cities_geography");
    }

    /**
     * Adds a city's data to the database.
     *
     * @param city The {@link ar.edu.itba.spatial_db.tpe.pg_loader.util.CitiesGeoJsonDataReader.CitiesGeoJsonDto}
     *             containing the data.
     */
    private void addCitiesToDatabase(CitiesGeoJsonDataReader.CitiesGeoJsonDto.CityFeatureDto city) {
        Assert.isTrue(city.getGeometry().getType().equals("Point"), "Only points for cities");
        final var latitude = city.getGeometry().getCoordinates()[0];
        final var longitude = city.getGeometry().getCoordinates()[1];
        jdbcTemplate.update(
                "INSERT INTO cities_geography " +
                        "(location, name, department, population) " +
                        "VALUES " +
                        "(ST_GeomFromEWKT(?), ?, ?, ?) " +
                        "ON CONFLICT DO NOTHING",
                "SRID=4326;Point(" + latitude + longitude + ")",
                city.getProperties().getName(),
                city.getProperties().getDepartment(),
                city.getProperties().getPopulation()
        );
    }

    /**
     * Adds a sube branch's data to the database.
     *
     * @param branch The {@link ar.edu.itba.spatial_db.tpe.pg_loader.util.SubeBranchTsvDataReader.SubeBranchDto}
     *               containing the data.
     */
    private void addBranchToDatabase(final SubeBranchTsvDataReader.SubeBranchDto branch) {
        jdbcTemplate.update(
                "INSERT INTO sube_branches_geography " +
                        "(location, branch_type, " +
                        "score, is_reportable, description, company_id, " +
                        "city, address, province, open_time, type_value, types) " +
                        "VALUES " +
                        "(ST_GeomFromEWKT(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT DO NOTHING",
                "SRID=4326;Point(" + branch.getLongitude() + branch.getLatitude() + ")",
                branch.getBranchType(),
                branch.getScore(),
                branch.isReportable(),
                branch.getDescription(),
                branch.getCompanyId(),
                branch.getCity(),
                branch.getAddress(),
                branch.getProvince(),
                branch.getOpenTime(),
                branch.getType(),
                Arrays.stream(branch.getTypes())
                        .map(Object::toString)
                        .collect(Collectors.joining(",", "[", "]"))

        );
    }
}
