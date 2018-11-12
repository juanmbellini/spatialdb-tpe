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

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * An object in charge of reading sube branches data from external JSON file.
 */
@Component
public class SubeBranchTsvDataReader
        extends AbstractDataReader<SubeBranchTsvDataReader.SubeBranchDto> implements InitializingBean {

    /**
     * The resource with the branches data (in JSON format).
     */
    private final Resource branchesJsonDataFile;

    /**
     * The {@link ObjectMapper} used to map JSON files into POJOs.
     */
    private final ObjectMapper objectMapper;


    /**
     * Constructor.
     *
     * @param branchesJsonDataFile The resource with the branches data (in JSON format).
     */
    @Autowired
    public SubeBranchTsvDataReader(@Value("classpath:data/branches.json") final Resource branchesJsonDataFile) {
        this.branchesJsonDataFile = branchesJsonDataFile;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterPropertiesSet() {
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }

    @Override
    protected File provideFile() throws IOException {
        return branchesJsonDataFile.getFile();
    }

    @Override
    protected ObjectMapper provideObjectMapper() {
        return this.objectMapper;
    }

    @Override
    protected Class<SubeBranchDto> provideClass() {
        return SubeBranchDto.class;
    }

    /**
     * A Data Transfer Object used to read data from the Data JSON file.
     */
    public static final class SubeBranchDto {

        /**
         * The lon. coordinate of the location of the branch.
         */
        private final Double longitude;

        /**
         * The lat. coordinate of the location of the branch.
         */
        private final Double latitude;

        /**
         * The type of branch.
         */
        private final String branchType;

        /**
         * The score given to the branch.
         */
        private final Integer score;

        /**
         * The branch's description.
         */
        private final String description;

        /**
         * A flag indicating if the branch is reportable.
         */
        private final Boolean isReportable;

        /**
         * The company's id.
         */
        private final String companyId;

        /**
         * The city in which the branch resides.
         */
        private final String city;

        /**
         * The branch's address.
         */
        private final String address;

        /**
         * The province in which the branch resides.
         */
        private final String province;

        /**
         * The time in which the branch is open.
         */
        private final String openTime;

        /**
         * The branch's type id.
         */
        private final Integer type;

        /**
         * Indicates all the branch's type (repeated in the source data).
         */
        private final Integer[] types;


        /**
         * @param latitude     The lat. coordinate of the location of the branch.
         * @param longitude    The lon. coordinate of the location of the branch.
         * @param branchType   The type of branch.
         * @param score        The score given to the branch.
         * @param description  The branch's description.
         * @param isReportable A flag indicating if the branch is reportable.
         * @param companyId    The company's id.
         * @param city         The city in which the branch resides.
         * @param address      The branch's address.
         * @param province     The province in which the branch resides.
         * @param openTime     The time in which the branch is open.
         * @param type         The branch's type id.
         * @param types        Indicates all the branch's type (repeated in the source data).
         */
        @JsonCreator
        public SubeBranchDto(
                @JsonProperty(value = "lon", access = WRITE_ONLY) final Double longitude,
                @JsonProperty(value = "lat", access = WRITE_ONLY) final Double latitude,
                @JsonProperty(value = "branchType", access = WRITE_ONLY) final String branchType,
                @JsonProperty(value = "score", access = WRITE_ONLY) final Integer score,
                @JsonProperty(value = "description", access = WRITE_ONLY) final String description,
                @JsonProperty(value = "isReportable", access = WRITE_ONLY) final Boolean isReportable,
                @JsonProperty(value = "companyId", access = WRITE_ONLY) final String companyId,
                @JsonProperty(value = "city", access = WRITE_ONLY) final String city,
                @JsonProperty(value = "address", access = WRITE_ONLY) final String address,
                @JsonProperty(value = "province", access = WRITE_ONLY) final String province,
                @JsonProperty(value = "openTime", access = WRITE_ONLY) final String openTime,
                @JsonProperty(value = "type", access = WRITE_ONLY) final Integer type,
                @JsonProperty(value = "types", access = WRITE_ONLY) final Integer[] types) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.branchType = branchType;
            this.score = score;
            this.description = description;
            this.isReportable = isReportable;
            this.companyId = companyId;
            this.city = city;
            this.address = address;
            this.province = province;
            this.openTime = openTime;
            this.type = type;
            this.types = types;
        }


        /**
         * @return The lat. coordinate of the location of the branch.
         */
        public Double getLatitude() {
            return latitude;
        }

        /**
         * @return The lon. coordinate of the location of the branch.
         */
        public Double getLongitude() {
            return longitude;
        }

        /**
         * @return The type of branch.
         */
        public String getBranchType() {
            return branchType;
        }

        /**
         * @return The score given to the branch.
         */
        public Integer getScore() {
            return score;
        }

        /**
         * @return The branch's description.
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return A flag indicating if the branch is reportable.
         */
        public Boolean isReportable() {
            return isReportable;
        }

        /**
         * @return The company's id.
         */
        public String getCompanyId() {
            return companyId;
        }

        /**
         * @return The city in which the branch resides.
         */
        public String getCity() {
            return city;
        }

        /**
         * @return The branch's address.
         */
        public String getAddress() {
            return address;
        }

        /**
         * @return The province in which the branch resides.
         */
        public String getProvince() {
            return province;
        }

        /**
         * @return The time in which the branch is open.
         */
        public String getOpenTime() {
            return openTime;
        }

        /**
         * @return The branch's type id.
         */
        public Integer getType() {
            return type;
        }

        /**
         * @return Indicates all the branch's type (repeated in the source data).
         */
        public Integer[] getTypes() {
            return types;
        }
    }
}
