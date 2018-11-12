package ar.edu.itba.spatial_db.tpe.pg_loader.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * An object in charge of reading cities data from external GeoJson file.
 */
@Component
public class CitiesGeoJsonDataReader
        extends AbstractDataReader<CitiesGeoJsonDataReader.CitiesGeoJsonDto> implements InitializingBean {

    /**
     * The resource with the branches data (in JSON format).
     */
    private final Resource citiesGeoJsonDataFile;

    /**
     * The {@link ObjectMapper} used to map JSON files into POJOs.
     */
    private final ObjectMapper objectMapper;

    @Autowired
    public CitiesGeoJsonDataReader(@Value("classpath:data/cities.geojson") final Resource citiesGeoJsonDataFile) {
        this.citiesGeoJsonDataFile = citiesGeoJsonDataFile;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterPropertiesSet() {
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }


    @Override
    protected InputStream provideInputStream() throws IOException {
        return citiesGeoJsonDataFile.getInputStream();
    }

    @Override
    protected ObjectMapper provideObjectMapper() {
        return this.objectMapper;
    }

    @Override
    protected Class<CitiesGeoJsonDto> provideClass() {
        return CitiesGeoJsonDto.class;
    }

    /**
     * Data transfer object for cities in the GeoJson file.
     */
    public static final class CitiesGeoJsonDto {

        /**
         * The features.
         */
        private final List<CityFeatureDto> features;

        /**
         * Constructor.
         *
         * @param features The features.
         */
        @JsonCreator
        public CitiesGeoJsonDto(
                @JsonProperty(value = "features", access = WRITE_ONLY) final List<CityFeatureDto> features) {
            this.features = features;
        }

        /**
         * @return The features.
         */
        public List<CityFeatureDto> getFeatures() {
            return features;
        }

        /**
         * Data transfer object for a city feature.
         */
        public static final class CityFeatureDto {

            /**
             * The type.
             */
            private final String type;

            /**
             * The properties of this city feature.
             */
            private final CityPropertiesDto properties;

            /**
             * The geometry of this city feature.
             */
            private final GeometryDto geometry;

            /**
             * Constructor.
             *
             * @param type       The type.
             * @param properties The properties of this city feature.
             * @param geometry   The geometry of this city feature.
             */
            @JsonCreator
            public CityFeatureDto(
                    @JsonProperty(value = "type", access = WRITE_ONLY) final String type,
                    @JsonProperty(value = "properties", access = WRITE_ONLY) final CityPropertiesDto properties,
                    @JsonProperty(value = "geometry", access = WRITE_ONLY) final GeometryDto geometry) {
                this.type = type;
                this.properties = properties;
                this.geometry = geometry;
            }

            /**
             * @return The type.
             */
            public String getType() {
                return type;
            }

            /**
             * @return The properties of this city feature.
             */
            public CityPropertiesDto getProperties() {
                return properties;
            }

            /**
             * @return The geometry of this city feature.
             */
            public GeometryDto getGeometry() {
                return geometry;
            }

            /**
             * A data transfer object for cities features properties.
             */
            public static final class CityPropertiesDto {
                /**
                 * The name of the city.
                 */
                private final String name;

                /**
                 * The type of the city.
                 */
                private final String type;

                /**
                 * The province
                 */
                private final String province;

                /**
                 * The department.
                 */
                private final String department;

                /**
                 * The sign.
                 */
                private final String sign;

                /**
                 * The population.
                 */
                private final int population;

                /**
                 * The source of the data.
                 */
                private final String source;

                /**
                 * Constructor.
                 *
                 * @param name       The name of the city.
                 * @param type       The type of the city.
                 * @param province   The province
                 * @param department The department.
                 * @param sign       The sign.
                 * @param population The population.
                 * @param source     The source of the data.
                 */
                @JsonCreator
                public CityPropertiesDto(
                        @JsonProperty(value = "nombre", access = WRITE_ONLY) final String name,
                        @JsonProperty(value = "tipo", access = WRITE_ONLY) final String type,
                        @JsonProperty(value = "provincia", access = WRITE_ONLY) final String province,
                        @JsonProperty(value = "departamento", access = WRITE_ONLY) final String department,
                        @JsonProperty(value = "signo", access = WRITE_ONLY) final String sign,
                        @JsonProperty(value = "canthab", access = WRITE_ONLY) final int population,
                        @JsonProperty(value = "fuente", access = WRITE_ONLY) final String source) {
                    this.name = name;
                    this.type = type;
                    this.province = province;
                    this.department = department;
                    this.sign = sign;
                    this.population = population;
                    this.source = source;
                }

                /**
                 * @return The name of the city.
                 */
                public String getName() {
                    return name;
                }

                /**
                 * @return The type of the city.
                 */
                public String getType() {
                    return type;
                }

                /**
                 * @return The province
                 */
                public String getProvince() {
                    return province;
                }

                /**
                 * @return The department.
                 */
                public String getDepartment() {
                    return department;
                }

                /**
                 * @return The sign.
                 */
                public String getSign() {
                    return sign;
                }

                /**
                 * @return The population.
                 */
                public int getPopulation() {
                    return population;
                }

                /**
                 * @return The source of the data.
                 */
                public String getSource() {
                    return source;
                }
            }


            /**
             * A data transfer object for geometries
             */
            public static final class GeometryDto {

                /**
                 * Type of geometry.
                 */
                private final String type;

                /**
                 * Coordinates.
                 */
                private final Double[] coordinates;

                /**
                 * Constructor.
                 *
                 * @param type        Type of geometry.
                 * @param coordinates Coordinates.
                 */
                @JsonCreator
                public GeometryDto(
                        @JsonProperty(value = "type", access = WRITE_ONLY) final String type,
                        @JsonProperty(value = "coordinates", access = WRITE_ONLY) final Double[] coordinates) {
                    this.type = type;
                    this.coordinates = coordinates;
                }

                /**
                 * @return Type of geometry.
                 */
                public String getType() {
                    return type;
                }

                /**
                 * @return Coordinates.
                 */
                public Double[] getCoordinates() {
                    return coordinates;
                }
            }

        }

    }
}
