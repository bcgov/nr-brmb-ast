package ca.bc.gov.farms.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CodeTableListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testGetCodeTableList() throws Exception {

        mockMvc.perform(get("/codeTables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CodeTableListRsrc"))
                .andExpect(jsonPath("$.codeTableList[0].codeTableName").value("farm_chef_form_type_codes"))
                .andExpect(jsonPath("$.codeTableList[1].codeTableName").value("farm_chef_submssn_status_codes"))
                .andExpect(jsonPath("$.codeTableList[2].codeTableName").value("farm_commodity_type_codes"))
                .andExpect(jsonPath("$.codeTableList[3].codeTableName").value("farm_config_param_type_codes"))
                .andExpect(jsonPath("$.codeTableList[4].codeTableName").value("farm_crm_entity_type_codes"))
                .andExpect(jsonPath("$.codeTableList[5].codeTableName").value("farm_crop_unit_codes"))
                .andExpect(jsonPath("$.codeTableList[6].codeTableName").value("farm_enrolment_calc_type_codes"))
                .andExpect(jsonPath("$.codeTableList[7].codeTableName").value("farm_farm_type_codes"))
                .andExpect(jsonPath("$.codeTableList[8].codeTableName").value("farm_federal_accounting_codes"))
                .andExpect(jsonPath("$.codeTableList[9].codeTableName").value("farm_federal_status_codes"))
                .andExpect(jsonPath("$.codeTableList[10].codeTableName").value("farm_fruit_veg_type_codes"))
                .andExpect(jsonPath("$.codeTableList[11].codeTableName").value("farm_import_class_codes"))
                .andExpect(jsonPath("$.codeTableList[12].codeTableName").value("farm_import_state_codes"))
                .andExpect(jsonPath("$.codeTableList[13].codeTableName").value("farm_inventory_class_codes"))
                .andExpect(jsonPath("$.codeTableList[14].codeTableName").value("farm_inventory_group_codes"))
                .andExpect(jsonPath("$.codeTableList[15].codeTableName").value("farm_inventory_item_codes"))
                .andExpect(jsonPath("$.codeTableList[16].codeTableName").value("farm_message_type_codes"))
                .andExpect(jsonPath("$.codeTableList[17].codeTableName").value("farm_multi_stage_commdty_codes"))
                .andExpect(jsonPath("$.codeTableList[18].codeTableName").value("farm_municipality_codes"))
                .andExpect(jsonPath("$.codeTableList[19].codeTableName").value("farm_participant_class_codes"))
                .andExpect(jsonPath("$.codeTableList[20].codeTableName").value("farm_participant_lang_codes"))
                .andExpect(jsonPath("$.codeTableList[21].codeTableName").value("farm_participant_profile_codes"))
                .andExpect(jsonPath("$.codeTableList[22].codeTableName").value("farm_participnt_data_src_codes"))
                .andExpect(jsonPath("$.codeTableList[23].codeTableName").value("farm_regional_office_codes"))
                .andExpect(jsonPath("$.codeTableList[24].codeTableName").value("farm_scenario_bpu_purpos_codes"))
                .andExpect(jsonPath("$.codeTableList[25].codeTableName").value("farm_scenario_category_codes"))
                .andExpect(jsonPath("$.codeTableList[26].codeTableName").value("farm_scenario_class_codes"))
                .andExpect(jsonPath("$.codeTableList[27].codeTableName").value("farm_scenario_state_codes"))
                .andExpect(jsonPath("$.codeTableList[28].codeTableName").value("farm_structural_change_codes"))
                .andExpect(jsonPath("$.codeTableList[29].codeTableName").value("farm_structure_group_codes"))
                .andExpect(jsonPath("$.codeTableList[30].codeTableName").value("farm_subscription_status_codes"))
                .andExpect(jsonPath("$.codeTableList[31].codeTableName").value("farm_tip_rating_codes"))
                .andExpect(jsonPath("$.codeTableList[32].codeTableName").value("farm_triage_queue_codes"));

    }
}
