ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT farm_rpfvr_farm_cuc_fk FOREIGN KEY (qty_produced_crop_unit_code) REFERENCES farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT farm_rpfvr_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
