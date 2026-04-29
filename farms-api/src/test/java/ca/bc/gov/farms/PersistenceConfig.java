package ca.bc.gov.farms;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import ca.bc.gov.brmb.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.brmb.common.checkhealth.CompositeValidator;
import ca.bc.gov.farms.common.validators.FarmsDatabaseCheckHealthValidator;

@Configuration
public class PersistenceConfig {

    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("farmsdev")
            .withUsername("app_farms")
            .withPassword("test1234")
            .withInitScripts(
                    "farms/schema.sql",
                    "farms/extensions.sql",
                    "farms/sequences.sql",
                    "farms/tables/farms.farm_agristabilty_cmmdty_xref.sql",
                    "farms/tables/farms.farm_benchmark_per_units.sql",
                    "farms/tables/farms.farm_benchmark_years.sql",
                    "farms/tables/farms.farm_chef_form_type_codes.sql",
                    "farms/tables/farms.farm_chef_submssn_status_codes.sql",
                    "farms/tables/farms.farm_commodity_type_codes.sql",
                    "farms/tables/farms.farm_config_param_type_codes.sql",
                    "farms/tables/farms.farm_configuration_parameters.sql",
                    "farms/tables/farms.farm_crm_entity_type_codes.sql",
                    "farms/tables/farms.farm_crop_unit_codes.sql",
                    "farms/tables/farms.farm_crop_unit_conversn_fctrs.sql",
                    "farms/tables/farms.farm_crop_unit_defaults.sql",
                    "farms/tables/farms.farm_enrolment_calc_type_codes.sql",
                    "farms/tables/farms.farm_expected_productions.sql",
                    "farms/tables/farms.farm_fair_market_values.sql",
                    "farms/tables/farms.farm_farm_type_codes.sql",
                    "farms/tables/farms.farm_federal_accounting_codes.sql",
                    "farms/tables/farms.farm_federal_status_codes.sql",
                    "farms/tables/farms.farm_fruit_veg_type_codes.sql",
                    "farms/tables/farms.farm_fruit_veg_type_details.sql",
                    "farms/tables/farms.farm_import_class_codes.sql",
                    "farms/tables/farms.farm_import_state_codes.sql",
                    "farms/tables/farms.farm_inventory_class_codes.sql",
                    "farms/tables/farms.farm_inventory_group_codes.sql",
                    "farms/tables/farms.farm_inventory_item_attributes.sql",
                    "farms/tables/farms.farm_inventory_item_codes.sql",
                    "farms/tables/farms.farm_inventory_item_details.sql",
                    "farms/tables/farms.farm_line_items.sql",
                    "farms/tables/farms.farm_market_rate_premium.sql",
                    "farms/tables/farms.farm_message_type_codes.sql",
                    "farms/tables/farms.farm_multi_stage_commdty_codes.sql",
                    "farms/tables/farms.farm_municipality_codes.sql",
                    "farms/tables/farms.farm_participant_class_codes.sql",
                    "farms/tables/farms.farm_participant_lang_codes.sql",
                    "farms/tables/farms.farm_participant_profile_codes.sql",
                    "farms/tables/farms.farm_participnt_data_src_codes.sql",
                    "farms/tables/farms.farm_regional_office_codes.sql",
                    "farms/tables/farms.farm_scenario_bpu_purpos_codes.sql",
                    "farms/tables/farms.farm_scenario_category_codes.sql",
                    "farms/tables/farms.farm_scenario_class_codes.sql",
                    "farms/tables/farms.farm_scenario_state_codes.sql",
                    "farms/tables/farms.farm_structural_change_codes.sql",
                    "farms/tables/farms.farm_structure_group_attributs.sql",
                    "farms/tables/farms.farm_structure_group_codes.sql",
                    "farms/tables/farms.farm_subscription_status_codes.sql",
                    "farms/tables/farms.farm_tip_rating_codes.sql",
                    "farms/tables/farms.farm_triage_queue_codes.sql",
                    "farms/tables/farms.farm_urls.sql",
                    "farms/tables/farms.farm_year_configuration_params.sql",
                    "farms/tables/foreign_keys.sql",
                    "farms/tables/data.sql");

    static {
        POSTGRES.start();
    }

    @Bean
    public DataSource farmsDataSource() {
        return DataSourceBuilder.create()
                .url(POSTGRES.getJdbcUrl())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .driverClassName(POSTGRES.getDriverClassName())
                .build();
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
