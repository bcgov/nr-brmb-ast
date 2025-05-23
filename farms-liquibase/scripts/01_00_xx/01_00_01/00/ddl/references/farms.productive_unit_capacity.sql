ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farming_operation(farming_operation_id)
;

ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_pdsc 
    FOREIGN KEY (participant_data_source_code)
    REFERENCES farms.participant_data_source_code(participant_data_source_code)
;

ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_puc 
    FOREIGN KEY (cra_productive_unit_capacity_id)
    REFERENCES farms.productive_unit_capacity(productve_unit_capacity_id)
;

ALTER TABLE farms.productive_unit_capacity ADD CONSTRAINT fk_puc_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.structure_group_code(structure_group_code)
;
