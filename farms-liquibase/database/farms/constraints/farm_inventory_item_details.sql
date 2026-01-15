ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ctc_fk FOREIGN KEY (commodity_type_code) REFERENCES farms.farm_commodity_type_codes(commodity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_farm_mscc_fk FOREIGN KEY (multi_stage_commdty_code) REFERENCES farms.farm_multi_stage_commdty_codes(multi_stage_commdty_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
