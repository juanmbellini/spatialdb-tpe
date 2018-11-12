package ar.edu.itba.spatial_db.tpe.pg_loader.persistence;

/**
 * A Repository for cities.
 */
public interface DatabaseWrapper {

    /**
     * Populates the database with data in resources.
     */
    void populateDatabase();

    /**
     * Clears the database.
     */
    void dropData();
}
