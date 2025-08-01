ALTER TABLE farms.farm_rsn_prdctn_grain_results ADD CONSTRAINT farm_rpgr_farm_cuc_fk FOREIGN KEY (qty_produced_crop_unit_code) REFERENCES farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_prdctn_grain_results ADD CONSTRAINT farm_rpgr_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
