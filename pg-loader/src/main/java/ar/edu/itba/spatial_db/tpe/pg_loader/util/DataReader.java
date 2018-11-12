package ar.edu.itba.spatial_db.tpe.pg_loader.util;

import com.fasterxml.jackson.databind.MappingIterator;

import java.io.IOException;

/**
 * Defines behaviour for an object that can read data from resources.
 */
public interface DataReader<DTO> {

    /**
     * Reads data from a data file in the resources of the app.
     *
     * @return A {@link MappingIterator} with the read data.
     * @throws IOException If any IO error occurs while reading data.
     */
    MappingIterator<DTO> readData() throws IOException;
}
