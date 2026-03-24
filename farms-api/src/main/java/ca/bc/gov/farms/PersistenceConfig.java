package ca.bc.gov.farms;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.bc.gov.brmb.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.brmb.common.checkhealth.CompositeValidator;
import ca.bc.gov.farms.common.validators.FarmsDatabaseCheckHealthValidator;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    @Bean
    public DataSource farmsDataSource() {
        DataSource result;

        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        result = dsLookup.getDataSource("java:comp/env/jdbc/farms_rest");

        return result;
    }

    @Bean(initMethod = "init")
    public CompositeValidator checkHealthValidator() {
        CompositeValidator result;

        result = new CompositeValidator();
        result.setComponentIdentifier("FARMS_REST");
        result.setComponentName("Farmer Access to Risk Management Service V1 Rest API");
        result.setValidators(healthCheckValidators());

        return result;
    }

    @Bean
    public List<CheckHealthValidator> healthCheckValidators() {
        List<CheckHealthValidator> result = new ArrayList<>();

        result.add(databaseCheckHealthValidator());

        return result;
    }

    @Bean(initMethod = "init")
    public FarmsDatabaseCheckHealthValidator databaseCheckHealthValidator() {
        FarmsDatabaseCheckHealthValidator result;

        result = new FarmsDatabaseCheckHealthValidator();
        result.setUsername("proxy_farms_rest");
        result.setDescription("java:comp/env/jdbc/farms_rest");
        result.setDataSource(farmsDataSource());

        return result;
    }
}
