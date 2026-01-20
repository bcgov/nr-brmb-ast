ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT farm_rrhi_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT farm_rrhi_farm_rrhr_fk FOREIGN KEY (rsn_rev_hog_result_id) REFERENCES farms.farm_rsn_rev_hog_results(rsn_rev_hog_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
