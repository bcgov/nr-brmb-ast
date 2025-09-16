package ca.bc.gov.farms.api.rest.v1.spring;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class EndpointsSpringConfigTest {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:17-alpine")
                .withDatabaseName("farmsdev")
                .withUsername("app_farms")
                .withPassword("test1234")
                .withInitScripts(
                        "farms/schema.sql",
                        "farms/extensions.sql",
                        "farms/sequences.sql",
                        "farms/tables/farms.farm_agristabilty_cmmdty_xref.sql",
                        "farms/tables/farms.farm_benchmark_years.sql",
                        "farms/tables/farms.farm_benchmark_per_units.sql",
                        "farms/tables/farms.farm_commodity_type_codes.sql",
                        "farms/tables/farms.farm_config_param_type_codes.sql",
                        "farms/tables/farms.farm_configuration_parameters.sql",
                        "farms/tables/farms.farm_crop_unit_codes.sql",
                        "farms/tables/farms.farm_crop_unit_defaults.sql",
                        "farms/tables/farms.farm_fair_market_values.sql",
                        "farms/tables/farms.farm_fruit_veg_type_codes.sql",
                        "farms/tables/farms.farm_inventory_class_codes.sql",
                        "farms/tables/farms.farm_inventory_group_codes.sql",
                        "farms/tables/farms.farm_inventory_item_attributes.sql",
                        "farms/tables/farms.farm_inventory_item_codes.sql",
                        "farms/tables/farms.farm_inventory_item_details.sql",
                        "farms/tables/farms.farm_multi_stage_commdty_codes.sql",
                        "farms/tables/farms.farm_municipality_codes.sql",
                        "farms/tables/farms.farm_structure_group_attributs.sql",
                        "farms/tables/farms.farm_structure_group_codes.sql",
                        "farms/tables/foreign_keys.sql",
                        "farms/tables/data.sql");
    }

    @Primary
    @Bean
    public DataSource farmsDataSource(PostgreSQLContainer<?> postgresContainer) {
        return DataSourceBuilder.create()
                .url(postgresContainer.getJdbcUrl())
                .username(postgresContainer.getUsername())
                .password(postgresContainer.getPassword())
                .driverClassName(postgresContainer.getDriverClassName())
                .build();
    }
}
