ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_farm_acx_fk FOREIGN KEY (agristabilty_cmmdty_xref_id) REFERENCES farms.farm_agristabilty_cmmdty_xref(agristabilty_cmmdty_xref_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_farm_ri_fk FOREIGN KEY (cra_reported_inventory_id) REFERENCES farms.farm_reported_inventories(reported_inventory_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
