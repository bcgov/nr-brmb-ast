package ca.bc.gov.farms.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.CodeRsrc;
import ca.bc.gov.farms.data.models.CodeTableListModel;
import ca.bc.gov.farms.data.models.CodeTableModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CodeServiceTest {

    @Autowired
    private CodeService codeService;

    private static final String TABLE_NAME = "farm_municipality_codes";
    private static final LocalDate EFFECTIVE_DATE = LocalDate.now();
    private static final LocalDate EXPIRY_DATE = EFFECTIVE_DATE.plusYears(1);

    @Test
    @Order(1)
    public void testCreateCode() {
        CodeRsrc resource = new CodeRsrc();
        resource.setCode("45");
        resource.setDescription("Test Municipality");
        resource.setEffectiveDate(EFFECTIVE_DATE);
        resource.setExpiryDate(EXPIRY_DATE);
        resource.setUserEmail("testUser");

        CodeRsrc newResource = codeService.createCode(TABLE_NAME, resource);

        assertThat(newResource.getCode()).isEqualTo("45");
        assertThat(newResource.getDescription()).isEqualTo("Test Municipality");
        assertThat(newResource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE);
        assertThat(newResource.getExpiryDate()).isEqualTo(EXPIRY_DATE);
    }

    @Test
    @Order(2)
    public void testGetCode() {
        CodeRsrc resource = null;
        try {
            resource = codeService.getCode(TABLE_NAME, "45");
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        assertThat(resource.getCode()).isEqualTo("45");
        assertThat(resource.getDescription()).isEqualTo("Test Municipality");
        assertThat(resource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE);
        assertThat(resource.getExpiryDate()).isEqualTo(EXPIRY_DATE);
    }

    @Test
    @Order(3)
    public void testGetCodeTable() {
        CodeTableModel resource = null;
        CodeRsrc codeResource = null;
        try {
            resource = codeService.getCodeTable(TABLE_NAME);
        } catch (ServiceException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(resource.getCodeTableName()).isEqualTo(TABLE_NAME);
        assertThat(resource.getCodeTableDescriptiveName()).isEqualTo(TABLE_NAME);
        codeResource = resource.getCodes().get(0);
        assertThat(codeResource.getCode()).isEqualTo("41");
        assertThat(codeResource.getDescription()).isEqualTo("Cariboo");
        assertThat(codeResource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE.toString());
        assertThat(codeResource.getExpiryDate()).isEqualTo("9999-12-31");
        codeResource = resource.getCodes().get(1);
        assertThat(codeResource.getCode()).isEqualTo("43");
        assertThat(codeResource.getDescription()).isEqualTo("Mount Waddington (Island part)");
        assertThat(codeResource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE.toString());
        assertThat(codeResource.getExpiryDate()).isEqualTo("9999-12-31");
        codeResource = resource.getCodes().get(2);
        assertThat(codeResource.getCode()).isEqualTo("45");
        assertThat(codeResource.getDescription()).isEqualTo("Test Municipality");
        assertThat(codeResource.getEffectiveDate()).isEqualTo(EFFECTIVE_DATE.toString());
        assertThat(codeResource.getExpiryDate()).isEqualTo(EXPIRY_DATE.toString());
    }

    @Test
    @Order(4)
    public void testGetCodeTableList() {
        CodeTableListModel resource = null;
        try {
            resource = codeService.getCodeTableList();
        } catch (ServiceException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(resource.getCodeTableList()).isNotNull();
        assertThat(resource.getCodeTableList()).isNotEmpty();
        assertThat(resource.getCodeTableList().size()).isEqualTo(35);
        assertThat(resource.getCodeTableList().get(0).getCodeTableName()).isEqualTo("farm_chef_form_type_codes");
        assertThat(resource.getCodeTableList().get(1).getCodeTableName()).isEqualTo("farm_chef_submssn_status_codes");
        assertThat(resource.getCodeTableList().get(2).getCodeTableName()).isEqualTo("farm_commodity_type_codes");
        assertThat(resource.getCodeTableList().get(3).getCodeTableName()).isEqualTo("farm_config_param_type_codes");
        assertThat(resource.getCodeTableList().get(4).getCodeTableName()).isEqualTo("farm_crm_entity_type_codes");
        assertThat(resource.getCodeTableList().get(5).getCodeTableName()).isEqualTo("farm_crop_unit_codes");
        assertThat(resource.getCodeTableList().get(6).getCodeTableName()).isEqualTo("farm_enrolment_calc_type_codes");
        assertThat(resource.getCodeTableList().get(7).getCodeTableName()).isEqualTo("farm_farm_type_codes");
        assertThat(resource.getCodeTableList().get(8).getCodeTableName()).isEqualTo("farm_federal_accounting_codes");
        assertThat(resource.getCodeTableList().get(9).getCodeTableName()).isEqualTo("farm_federal_status_codes");
        assertThat(resource.getCodeTableList().get(10).getCodeTableName()).isEqualTo("farm_fruit_veg_type_codes");
        assertThat(resource.getCodeTableList().get(11).getCodeTableName()).isEqualTo("farm_import_class_codes");
        assertThat(resource.getCodeTableList().get(12).getCodeTableName()).isEqualTo("farm_import_state_codes");
        assertThat(resource.getCodeTableList().get(13).getCodeTableName()).isEqualTo("farm_inventory_class_codes");
        assertThat(resource.getCodeTableList().get(14).getCodeTableName()).isEqualTo("farm_inventory_group_codes");
        assertThat(resource.getCodeTableList().get(15).getCodeTableName()).isEqualTo("farm_inventory_item_codes");
        assertThat(resource.getCodeTableList().get(16).getCodeTableName()).isEqualTo("farm_message_type_codes");
        assertThat(resource.getCodeTableList().get(17).getCodeTableName()).isEqualTo("farm_multi_stage_commdty_codes");
        assertThat(resource.getCodeTableList().get(18).getCodeTableName()).isEqualTo("farm_municipality_codes");
        assertThat(resource.getCodeTableList().get(19).getCodeTableName()).isEqualTo("farm_participant_class_codes");
        assertThat(resource.getCodeTableList().get(20).getCodeTableName()).isEqualTo("farm_participant_lang_codes");
        assertThat(resource.getCodeTableList().get(21).getCodeTableName()).isEqualTo("farm_participant_profile_codes");
        assertThat(resource.getCodeTableList().get(22).getCodeTableName()).isEqualTo("farm_participnt_data_src_codes");
        assertThat(resource.getCodeTableList().get(23).getCodeTableName()).isEqualTo("farm_regional_office_codes");
        assertThat(resource.getCodeTableList().get(24).getCodeTableName()).isEqualTo("farm_scenario_bpu_purpos_codes");
        assertThat(resource.getCodeTableList().get(25).getCodeTableName()).isEqualTo("farm_scenario_category_codes");
        assertThat(resource.getCodeTableList().get(26).getCodeTableName()).isEqualTo("farm_scenario_class_codes");
        assertThat(resource.getCodeTableList().get(27).getCodeTableName()).isEqualTo("farm_scenario_state_codes");
        assertThat(resource.getCodeTableList().get(28).getCodeTableName()).isEqualTo("farm_sector_codes");
        assertThat(resource.getCodeTableList().get(29).getCodeTableName()).isEqualTo("farm_sector_detail_codes");
        assertThat(resource.getCodeTableList().get(30).getCodeTableName()).isEqualTo("farm_structural_change_codes");
        assertThat(resource.getCodeTableList().get(31).getCodeTableName()).isEqualTo("farm_structure_group_codes");
        assertThat(resource.getCodeTableList().get(32).getCodeTableName()).isEqualTo("farm_subscription_status_codes");
        assertThat(resource.getCodeTableList().get(33).getCodeTableName()).isEqualTo("farm_tip_rating_codes");
        assertThat(resource.getCodeTableList().get(34).getCodeTableName()).isEqualTo("farm_triage_queue_codes");
    }

    @Test
    @Order(5)
    public void testUpdateCode() {
        CodeRsrc resource = null;
        try {
            resource = codeService.getCode(TABLE_NAME, "45");
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setDescription("Municipality Test");
        resource.setEffectiveDate(EXPIRY_DATE);
        resource.setExpiryDate(EFFECTIVE_DATE);
        resource.setUserEmail("testUser");

        CodeRsrc updatedResource = null;
        try {
            updatedResource = codeService.updateCode(TABLE_NAME, "45", resource);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(resource.getCode()).isEqualTo("45");
        assertThat(updatedResource.getDescription()).isEqualTo("Municipality Test");
        assertThat(resource.getEffectiveDate()).isEqualTo(EXPIRY_DATE);
        assertThat(resource.getExpiryDate()).isEqualTo(EFFECTIVE_DATE);
    }

    @Test
    @Order(6)
    public void testDeleteCode() {
        CodeRsrc resource = new CodeRsrc();
        resource.setCode("45");
        resource.setDescription("Test Municipality");
        resource.setEffectiveDate(EFFECTIVE_DATE);
        resource.setExpiryDate(EXPIRY_DATE);
        resource.setUserEmail("testUser");

        assertThatNoException().isThrownBy(() -> {
            codeService.deleteCode(TABLE_NAME, "45", resource);
        });
    }
}
