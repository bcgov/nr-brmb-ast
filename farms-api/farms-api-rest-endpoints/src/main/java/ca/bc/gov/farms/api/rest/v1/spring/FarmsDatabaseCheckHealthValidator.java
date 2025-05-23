package ca.bc.gov.farms.api.rest.v1.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.brmb.common.checkhealth.DatabaseCheckHealthValidator;

public class FarmsDatabaseCheckHealthValidator extends DatabaseCheckHealthValidator {

    private final Logger logger = LoggerFactory.getLogger(FarmsDatabaseCheckHealthValidator.class);

    private String username;
    private String description;
    private DataSource dataSource;

    @Override
    public void init() {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null.");
        }
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null.");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null.");
        }
    }

    @Override
    public HealthCheckResponseRsrc validate(final String callstack) {
        logger.debug("<validate " + this.getComponentName());

        HealthCheckResponseRsrc result = new HealthCheckResponseRsrc();
        result.setComponentIdentifier(getComponentIdentifier());
        result.setComponentName(getComponentName());

        try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {

            st.execute("SELECT CURRENT_DATE");

            result.setValidationStatus(this.getGreenMapping());
            result.setStatusDetails(username + " database connection successful.");

        } catch (SQLException e) {
            result.setValidationStatus(this.getRedMapping());
            result.setStatusDetails(username + " database connection failed: " + e.getMessage());
        }

        logger.debug(">validate " + this.getComponentName());
        return result;
    }

    @Override
    public String getComponentIdentifier() {
        return username + " Database User";
    }

    @Override
    public String getComponentName() {
        return description;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
