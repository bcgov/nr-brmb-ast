ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT farm_rpfvr_farm_cuc_fk FOREIGN KEY (qty_produced_crop_unit_code) REFERENCES farms.farm_crop_unit_codes(crop_unit_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT farm_rpfvr_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT farm_rpfvr_farm_rtr_fk FOREIGN KEY (reasonability_test_result_id) REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
