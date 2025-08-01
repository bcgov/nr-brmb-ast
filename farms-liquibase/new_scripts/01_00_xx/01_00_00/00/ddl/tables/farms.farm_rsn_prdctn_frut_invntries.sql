ALTER TABLE farms.farm_rsn_prdctn_frut_invntries ADD CONSTRAINT farm_rpfi_farm_cuc_fk FOREIGN KEY (qty_produced_crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_prdctn_frut_invntries ADD CONSTRAINT farm_rpfi_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
