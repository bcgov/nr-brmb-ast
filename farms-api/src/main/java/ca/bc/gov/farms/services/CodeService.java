package ca.bc.gov.farms.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.CodeResourceAssembler;
import ca.bc.gov.farms.data.entities.CodeEntity;
import ca.bc.gov.farms.data.models.CodeModel;
import ca.bc.gov.farms.data.repositories.CodeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private CodeResourceAssembler codeResourceAssembler;

    private Map<String, String> codeNameMap = new HashMap<>() {
        {
            put("farm_chef_form_type_codes", "chef_form_type_code");
            put("farm_chef_submssn_status_codes", "chef_submssn_status_code");
            put("farm_commodity_type_codes", "commodity_type_code");
            put("farm_config_param_type_codes", "config_param_type_code");
            put("farm_crm_entity_type_codes", "crm_entity_type_code");
            put("farm_crop_unit_codes", "crop_unit_code");
            put("farm_enrolment_calc_type_codes", "enrolment_calc_type_code");
            put("farm_farm_type_codes", "farm_type_code");
            put("farm_federal_accounting_codes", "federal_accounting_code");
            put("farm_federal_status_codes", "federal_status_code");
            put("farm_fruit_veg_type_codes", "fruit_veg_type_code");
            put("farm_import_class_codes", "import_class_code");
            put("farm_import_state_codes", "import_state_code");
            put("farm_inventory_class_codes", "inventory_class_code");
            put("farm_inventory_group_codes", "inventory_group_code");
            put("farm_inventory_item_codes", "inventory_item_code");
            put("farm_message_type_codes", "message_type_code");
            put("farm_multi_stage_commdty_codes", "multi_stage_commdty_code");
            put("farm_municipality_codes", "municipality_code");
            put("farm_participant_class_codes", "participant_class_code");
            put("farm_participnt_data_src_codes", "participnt_data_src_code");
            put("farm_participant_lang_codes", "participant_lang_code");
            put("farm_participant_profile_codes", "participant_profile_code");
            put("farm_regional_office_codes", "regional_office_code");
            put("farm_scenario_bpu_purpos_codes", "scenario_bpu_purpose_code");
            put("farm_scenario_category_codes", "scenario_category_code");
            put("farm_scenario_class_codes", "scenario_class_code");
            put("farm_scenario_state_codes", "scenario_state_code");
            put("farm_sector_codes", "sector_code");
            put("farm_sector_detail_codes", "sector_detail_code");
            put("farm_structural_change_codes", "structural_change_code");
            put("farm_structure_group_codes", "structure_group_code");
            put("farm_subscription_status_codes", "subscription_status_code");
            put("farm_tip_rating_codes", "tip_rating_code");
            put("farm_triage_queue_codes", "triage_queue_code");
        }
    };

    public CodeModel getCode(String tableName, String codeValue) throws ServiceException, NotFoundException {

        CodeModel result = null;
        String codeName = codeNameMap.get(tableName);

        try {
            CodeEntity entity = codeRepository.fetchOne(tableName, codeName, codeValue);

            if (entity == null) {
                throw new NotFoundException("Did not find the code: " + codeValue);
            }

            result = codeResourceAssembler.getCode(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Repository threw an exception", t);
        }

        return result;
    }

    @Transactional
    public CodeModel createCode(String tableName, CodeModel resource) throws ServiceException {

        CodeModel result = null;
        String codeName = codeNameMap.get(tableName);
        String userId = resource.getUserEmail();

        try {

            CodeEntity entity = new CodeEntity();

            codeResourceAssembler.updateCode(resource, entity);
            int count = codeRepository.insert(tableName, codeName, entity, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            entity = codeRepository.fetchOne(tableName, codeName, entity.getCode());
            result = codeResourceAssembler.getCode(entity);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Repository threw an exception", t);
        }

        return result;
    }

    @Transactional
    public CodeModel updateCode(String tableName, String codeValue, CodeModel resource)
            throws ServiceException, NotFoundException {

        CodeModel result = null;
        String codeName = codeNameMap.get(tableName);
        String userId = resource.getUserEmail();

        try {

            CodeEntity entity = codeRepository.fetchOne(tableName, codeName, codeValue);

            if (entity == null) {
                throw new NotFoundException("Did not find the code: " + codeValue);
            }

            codeResourceAssembler.updateCode(resource, entity);
            int count = codeRepository.update(tableName, codeName, entity, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            entity = codeRepository.fetchOne(tableName, codeName, codeValue);
            result = codeResourceAssembler.getCode(entity);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Repository threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteCode(String tableName, String codeValue) throws ServiceException, NotFoundException {

        String codeName = codeNameMap.get(tableName);

        try {
            CodeEntity entity = codeRepository.fetchOne(tableName, codeName, codeValue);

            if (entity == null) {
                throw new NotFoundException("Did not find the code: " + codeValue);
            }

            int count = codeRepository.delete(tableName, codeName, codeValue);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Repository threw an exception", t);
        }
    }
}
