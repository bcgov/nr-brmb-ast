ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_benchmark_years ADD CONSTRAINT farm_bpuy_farm_bpu_fk FOREIGN KEY (benchmark_per_unit_id) REFERENCES farms.farm_benchmark_per_units(benchmark_per_unit_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ctc_fk FOREIGN KEY (commodity_type_code) REFERENCES farms.farm_commodity_type_codes(commodity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_mscc_fk FOREIGN KEY (multi_stage_commdty_code) REFERENCES farms.farm_multi_stage_commdty_codes(multi_stage_commdty_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
