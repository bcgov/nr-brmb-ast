ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT farm_nm_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT farm_nm_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT farm_nm_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
