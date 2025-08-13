package ca.bc.gov.farms.service.api.v1.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.farms.persistence.code.spring.CodePersistenceSpringConfig;
import ca.bc.gov.farms.service.api.v1.util.CachedCodeTables;
import ca.bc.gov.brmb.common.persistence.code.dao.CodeTableConfig;
import ca.bc.gov.brmb.common.persistence.code.spring.CodeHierarchySpringConfig;

@Configuration
@Import({
        CodeHierarchySpringConfig.class, // can't remove this because some brmb stuff depends on it
        CodePersistenceSpringConfig.class
})
public class CodeTableSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(CodeTableSpringConfig.class);

    @Autowired
    private CodePersistenceSpringConfig codePersistenceSpringConfig;

    public CodeTableSpringConfig() {
        logger.info("<CodeTableSpringConfig");

        logger.info(">CodeTableSpringConfig");
    }

    @Bean
    public CachedCodeTables cachedCodeTables() {
        CachedCodeTables result;

        result = new CachedCodeTables();
        result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
        result.setCodeTableConfigs(codeTableConfigs());

        return result;
    }

    @Bean
    public List<CodeTableConfig> codeTableConfigs() {
        List<CodeTableConfig> result = new ArrayList<>();

        result.add(chefFormTypeCodeConfig());
        result.add(chefSubmissionStatusCodeConfig());
        result.add(commodityTypeCodeConfig());
        result.add(configurationParameterTypeCodeConfig());
        result.add(crmEntityTypeCodeConfig());
        result.add(cropUnitCodeConfig());
        result.add(enrolmentCalculationTypeCodeConfig());
        result.add(farmTypeCodeConfig());
        result.add(federalAccountingCodeConfig());
        result.add(federalStatusCodeConfig());
        result.add(fruitVegetableTypeCodeConfig());
        result.add(importClassCodeConfig());
        result.add(importStateCodeConfig());
        result.add(inventoryClassCodeConfig());
        result.add(inventoryGroupCodeConfig());
        result.add(inventoryItemCodeConfig());
        result.add(messageTypeCodeConfig());
        result.add(multipleStageCommodityCodeConfig());
        result.add(municipalityCodeConfig());
        result.add(participantClassCodeConfig());
        result.add(participantDataSourceCodeConfig());
        result.add(participantLanguageCodeConfig());
        result.add(participantProfileCodeConfig());
        result.add(regionalOfficeCodeConfig());
        result.add(scenarioBpuPurposeCodeConfig());
        result.add(scenarioCategoryCodeConfig());
        result.add(scenarioClassCodeConfig());
        result.add(scenarioStateCodeConfig());
        result.add(sectorCodeConfig());
        result.add(sectorDetailCodeConfig());
        result.add(structuralChangeCodeConfig());
        result.add(structureGroupCodeConfig());
        result.add(subscriptionStatusCodeConfig());
        result.add(tipRatingCodeConfig());
        result.add(triageQueueCodeConfig());

        return result;
    }

    private CodeTableConfig chefFormTypeCodeConfig() {
        return createCodeTableConfig("farm_chef_form_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig chefSubmissionStatusCodeConfig() {
        return createCodeTableConfig("farm_chef_submssn_status_codes", "DESCRIPTION");
    }

    private CodeTableConfig commodityTypeCodeConfig() {
        return createCodeTableConfig("farm_commodity_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig configurationParameterTypeCodeConfig() {
        return createCodeTableConfig("farm_config_param_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig crmEntityTypeCodeConfig() {
        return createCodeTableConfig("farm_crm_entity_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig cropUnitCodeConfig() {
        return createCodeTableConfig("farm_crop_unit_codes", "DESCRIPTION");
    }

    private CodeTableConfig enrolmentCalculationTypeCodeConfig() {
        return createCodeTableConfig("farm_enrolment_calc_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig farmTypeCodeConfig() {
        return createCodeTableConfig("farm_farm_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig federalAccountingCodeConfig() {
        return createCodeTableConfig("farm_federal_accounting_codes", "DESCRIPTION");
    }

    private CodeTableConfig federalStatusCodeConfig() {
        return createCodeTableConfig("farm_federal_status_codes", "DESCRIPTION");
    }

    private CodeTableConfig fruitVegetableTypeCodeConfig() {
        return createCodeTableConfig("farm_fruit_veg_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig importClassCodeConfig() {
        return createCodeTableConfig("farm_import_class_codes", "DESCRIPTION");
    }

    private CodeTableConfig importStateCodeConfig() {
        return createCodeTableConfig("farm_import_state_codes", "DESCRIPTION");
    }

    private CodeTableConfig inventoryClassCodeConfig() {
        return createCodeTableConfig("farm_inventory_class_codes", "DESCRIPTION");
    }

    private CodeTableConfig inventoryGroupCodeConfig() {
        return createCodeTableConfig("farm_inventory_group_codes", "DESCRIPTION");
    }

    private CodeTableConfig inventoryItemCodeConfig() {
        return createCodeTableConfig("farm_inventory_item_codes", "DESCRIPTION");
    }

    private CodeTableConfig messageTypeCodeConfig() {
        return createCodeTableConfig("farm_message_type_codes", "DESCRIPTION");
    }

    private CodeTableConfig multipleStageCommodityCodeConfig() {
        return createCodeTableConfig("farm_multi_stage_commdty_codes", "DESCRIPTION");
    }

    private CodeTableConfig municipalityCodeConfig() {
        return createCodeTableConfig("farm_municipality_codes", "DESCRIPTION");
    }

    private CodeTableConfig participantClassCodeConfig() {
        return createCodeTableConfig("farm_participant_class_codes", "DESCRIPTION");
    }

    private CodeTableConfig participantDataSourceCodeConfig() {
        return createCodeTableConfig("farm_participnt_data_src_codes", "DESCRIPTION");
    }

    private CodeTableConfig participantLanguageCodeConfig() {
        return createCodeTableConfig("farm_participant_lang_codes", "DESCRIPTION");
    }

    private CodeTableConfig participantProfileCodeConfig() {
        return createCodeTableConfig("farm_participant_profile_codes", "DESCRIPTION");
    }

    private CodeTableConfig regionalOfficeCodeConfig() {
        return createCodeTableConfig("farm_regional_office_codes", "DESCRIPTION");
    }

    private CodeTableConfig scenarioBpuPurposeCodeConfig() {
        return createCodeTableConfig("farm_scenario_bpu_purpos_codes", "DESCRIPTION");
    }

    private CodeTableConfig scenarioCategoryCodeConfig() {
        return createCodeTableConfig("farm_scenario_category_codes", "DESCRIPTION");
    }

    private CodeTableConfig scenarioClassCodeConfig() {
        return createCodeTableConfig("farm_scenario_class_codes", "DESCRIPTION");
    }

    private CodeTableConfig scenarioStateCodeConfig() {
        return createCodeTableConfig("farm_scenario_state_codes", "DESCRIPTION");
    }

    private CodeTableConfig sectorCodeConfig() {
        return createCodeTableConfig("farm_sector_codes", "DESCRIPTION");
    }

    private CodeTableConfig sectorDetailCodeConfig() {
        return createCodeTableConfig("farm_sector_detail_codes", "DESCRIPTION");
    }

    private CodeTableConfig structuralChangeCodeConfig() {
        return createCodeTableConfig("farm_structural_change_codes", "DESCRIPTION");
    }

    private CodeTableConfig structureGroupCodeConfig() {
        return createCodeTableConfig("farm_structure_group_codes", "DESCRIPTION");
    }

    private CodeTableConfig subscriptionStatusCodeConfig() {
        return createCodeTableConfig("farm_subscription_status_codes", "DESCRIPTION");
    }

    private CodeTableConfig tipRatingCodeConfig() {
        return createCodeTableConfig("farm_tip_rating_codes", "DESCRIPTION");
    }

    private CodeTableConfig triageQueueCodeConfig() {
        return createCodeTableConfig("farm_triage_queue_codes", "DESCRIPTION");
    }

    private CodeTableConfig createCodeTableConfig(String tableName, String sortColumn) {
        CodeTableConfig result = new CodeTableConfig();

        String fetchSql = String.format(
                "SELECT T.%s CODE, T.DESCRIPTION, NULL DISPLAY_ORDER, T.ESTABLISHED_DATE, T.EXPIRY_DATE, T.REVISION_COUNT, T.WHO_CREATED, T.WHEN_CREATED, T.WHO_UPDATED, T.WHEN_UPDATED FROM %s T ORDER BY T."
                        + sortColumn,
                tableName, tableName);

        result.setUseRevisionCount(Boolean.TRUE);
        result.setUseDisplayOrder(Boolean.FALSE);
        result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
        result.setCodeTableName(tableName);
        result.setFetchSql(fetchSql);

        return result;
    }
}
