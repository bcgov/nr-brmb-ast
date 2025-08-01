ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT farm_rrni_farm_cuc_fk FOREIGN KEY (crop_unit_code) REFERENCES farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT farm_rrni_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
