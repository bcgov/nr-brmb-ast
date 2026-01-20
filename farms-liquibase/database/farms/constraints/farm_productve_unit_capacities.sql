ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_pdsc_fk FOREIGN KEY (participnt_data_src_code) REFERENCES farms.farm_participnt_data_src_codes(participnt_data_src_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_puc_fk FOREIGN KEY (cra_productve_unit_capacity_id) REFERENCES farms.farm_productve_unit_capacities(productve_unit_capacity_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
