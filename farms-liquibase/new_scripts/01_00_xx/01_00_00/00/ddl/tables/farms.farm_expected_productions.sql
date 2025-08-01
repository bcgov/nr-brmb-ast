ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
