ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT farm_rrni_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT farm_rrni_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT farm_rrni_farm_rrnr_fk FOREIGN KEY (rsn_rev_nursery_result_id) REFERENCES farms.farm_rsn_rev_nursery_results(rsn_rev_nursery_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
