package ar.edu.itba.spatial_db.tpe.pg_loader;

import ar.edu.itba.spatial_db.tpe.pg_loader.persistence.DatabaseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class.
 */
@SpringBootApplication
public class PGLoader implements CommandLineRunner {

    /**
     * The {@link Logger} object.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(PGLoader.class);

    /**
     * The execution mode.
     */
    private final Mode executionMode;

    /**
     * The {@link DatabaseWrapper} used to communicate with the database.
     */
    private final DatabaseWrapper databaseWrapper;

    @Autowired
    public PGLoader(@Value("${ar.edu.itba.spatial_db.tpe.pg_loader:}") final String executionMode,
                    final DatabaseWrapper databaseWrapper) {
        this.databaseWrapper = databaseWrapper;
        try {
            this.executionMode = Mode.valueOf(executionMode.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.error("The entered mode in not supported");
            throw new RuntimeException("The entered mode is not supported", e);
        }
    }

    @Override
    public void run(final String... args) {
        LOGGER.info("Starting program. Executing in {} mode.", executionMode.toString());
        executionMode.execute(databaseWrapper);
        LOGGER.info("Finished program");
    }


    /**
     * Entry point.
     *
     * @param args Program arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(PGLoader.class, args);
    }

    /**
     * The execution modes supported by this program.
     */
    public enum Mode {
        POPULATE {
            @Override
            void execute(final DatabaseWrapper databaseWrapper) {
                databaseWrapper.populateDatabase();
            }

            @Override
            public String toString() {
                return "populate";
            }
        },
        CLEAR {
            @Override
            void execute(final DatabaseWrapper databaseWrapper) {
                databaseWrapper.dropData();
            }

            @Override
            public String toString() {
                return "clear";
            }
        };

        /**
         * Executes the program according to the enum value.
         *
         * @param databaseWrapper The {@link DatabaseWrapper} used to communicate with the database.
         */
        /* package */
        abstract void execute(final DatabaseWrapper databaseWrapper);
    }
}
