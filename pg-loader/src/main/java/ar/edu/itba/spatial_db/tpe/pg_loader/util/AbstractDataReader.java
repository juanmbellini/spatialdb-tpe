package ar.edu.itba.spatial_db.tpe.pg_loader.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * An abstract data reader.
 */
public abstract class AbstractDataReader<DTO> implements DataReader<DTO> {
    @Override
    public MappingIterator<DTO> readData() throws IOException {
        final var reader = new FileReader(provideFile());
        return provideObjectMapper().readerFor(provideClass()).readValues(reader);
    }

    /**
     * @return The {@link File} from which data is read.
     */
    protected abstract File provideFile() throws IOException;

    /**
     * @return The {@link ObjectMapper} used to read data.
     */
    protected abstract ObjectMapper provideObjectMapper();

    /**
     * @return The {@link Class} for which this data reader is created.
     */
    protected abstract Class<DTO> provideClass();
}
