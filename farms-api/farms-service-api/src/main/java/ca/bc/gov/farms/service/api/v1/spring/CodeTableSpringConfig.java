package ca.bc.gov.farms.service.api.v1.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.farms.service.api.v1.util.CachedCodeTables;
import ca.bc.gov.brmb.common.persistence.code.dao.CodeTableConfig;
import ca.bc.gov.brmb.common.persistence.code.spring.CodePersistenceSpringConfig;

@Configuration
@Import({
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
        return createCodeTableConfig("chef_form_type_code", "DESCRIPTION");
    }

    private CodeTableConfig chefSubmissionStatusCodeConfig() {
        return createCodeTableConfig("chef_submission_status_code", "DESCRIPTION");
    }

    private CodeTableConfig commodityTypeCodeConfig() {
        return createCodeTableConfig("commodity_type_code", "DESCRIPTION");
    }

    private CodeTableConfig configurationParameterTypeCodeConfig() {
        return createCodeTableConfig("configuration_parameter_type_code", "DESCRIPTION");
    }

    private CodeTableConfig crmEntityTypeCodeConfig() {
        return createCodeTableConfig("crm_entity_type_code", "DESCRIPTION");
    }

    private CodeTableConfig cropUnitCodeConfig() {
        return createCodeTableConfig("crop_unit_code", "DESCRIPTION");
    }

    private CodeTableConfig enrolmentCalculationTypeCodeConfig() {
        return createCodeTableConfig("enrolment_calculation_type_code", "DESCRIPTION");
    }

    private CodeTableConfig farmTypeCodeConfig() {
        return createCodeTableConfig("farm_type_code", "DESCRIPTION");
    }

    private CodeTableConfig federalAccountingCodeConfig() {
        return createCodeTableConfig("federal_accounting_code", "DESCRIPTION");
    }

    private CodeTableConfig federalStatusCodeConfig() {
        return createCodeTableConfig("federal_status_code", "DESCRIPTION");
    }

    private CodeTableConfig fruitVegetableTypeCodeConfig() {
        return createCodeTableConfig("fruit_vegetable_type_code", "DESCRIPTION");
    }

    private CodeTableConfig importClassCodeConfig() {
        return createCodeTableConfig("import_class_code", "DESCRIPTION");
    }

    private CodeTableConfig importStateCodeConfig() {
        return createCodeTableConfig("import_state_code", "DESCRIPTION");
    }

    private CodeTableConfig inventoryClassCodeConfig() {
        return createCodeTableConfig("inventory_class_code", "DESCRIPTION");
    }

    private CodeTableConfig inventoryGroupCodeConfig() {
        return createCodeTableConfig("inventory_group_code", "DESCRIPTION");
    }

    private CodeTableConfig inventoryItemCodeConfig() {
        return createCodeTableConfig("inventory_item_code", "DESCRIPTION");
    }

    private CodeTableConfig messageTypeCodeConfig() {
        return createCodeTableConfig("message_type_code", "DESCRIPTION");
    }

    private CodeTableConfig multipleStageCommodityCodeConfig() {
        return createCodeTableConfig("multiple_stage_commodity_code", "DESCRIPTION");
    }

    private CodeTableConfig municipalityCodeConfig() {
        return createCodeTableConfig("municipality_code", "DESCRIPTION");
    }

    private CodeTableConfig participantClassCodeConfig() {
        return createCodeTableConfig("participant_class_code", "DESCRIPTION");
    }

    private CodeTableConfig participantDataSourceCodeConfig() {
        return createCodeTableConfig("participant_data_source_code", "DESCRIPTION");
    }

    private CodeTableConfig participantLanguageCodeConfig() {
        return createCodeTableConfig("participant_language_code", "DESCRIPTION");
    }

    private CodeTableConfig participantProfileCodeConfig() {
        return createCodeTableConfig("participant_profile_code", "DESCRIPTION");
    }

    private CodeTableConfig regionalOfficeCodeConfig() {
        return createCodeTableConfig("regional_office_code", "DESCRIPTION");
    }

    private CodeTableConfig scenarioBpuPurposeCodeConfig() {
        return createCodeTableConfig("scenario_bpu_purpose_code", "DESCRIPTION");
    }

    private CodeTableConfig scenarioCategoryCodeConfig() {
        return createCodeTableConfig("scenario_category_code", "DESCRIPTION");
    }

    private CodeTableConfig scenarioClassCodeConfig() {
        return createCodeTableConfig("scenario_class_code", "DESCRIPTION");
    }

    private CodeTableConfig scenarioStateCodeConfig() {
        return createCodeTableConfig("scenario_state_code", "DESCRIPTION");
    }

    private CodeTableConfig sectorCodeConfig() {
        return createCodeTableConfig("sector_code", "DESCRIPTION");
    }

    private CodeTableConfig sectorDetailCodeConfig() {
        return createCodeTableConfig("sector_detail_code", "DESCRIPTION");
    }

    private CodeTableConfig structuralChangeCodeConfig() {
        return createCodeTableConfig("structural_change_code", "DESCRIPTION");
    }

    private CodeTableConfig structureGroupCodeConfig() {
        return createCodeTableConfig("structure_group_code", "DESCRIPTION");
    }

    private CodeTableConfig subscriptionStatusCodeConfig() {
        return createCodeTableConfig("subscription_status_code", "DESCRIPTION");
    }

    private CodeTableConfig tipRatingCodeConfig() {
        return createCodeTableConfig("tip_rating_code", "DESCRIPTION");
    }

    private CodeTableConfig triageQueueCodeConfig() {
        return createCodeTableConfig("triage_queue_code", "DESCRIPTION");
    }

    private CodeTableConfig createCodeTableConfig(String tableName, String sortColumn) {
        CodeTableConfig result = new CodeTableConfig();

        String fetchSql = String.format(
                "SELECT T.%s CODE, T.DESCRIPTION, NULL DISPLAY_ORDER, T.ESTABLISHED_DATE, T.EXPIRY_DATE, T.REVISION_COUNT, T.CREATE_USER, T.CREATE_DATE, T.UPDATE_USER, T.UPDATE_DATE FROM %s T ORDER BY T."
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
