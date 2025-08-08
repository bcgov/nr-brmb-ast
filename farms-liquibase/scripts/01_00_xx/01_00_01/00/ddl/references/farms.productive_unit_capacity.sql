ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_pdsc 
    FOREIGN KEY (participnt_data_src_code)
    REFERENCES farms.farm_participnt_data_src_codes(participnt_data_src_code)
;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_puc 
    FOREIGN KEY (cra_productve_unit_capacity_id)
    REFERENCES farms.farm_productve_unit_capacities(productve_unit_capacity_id)
;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT fk_puc_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;
