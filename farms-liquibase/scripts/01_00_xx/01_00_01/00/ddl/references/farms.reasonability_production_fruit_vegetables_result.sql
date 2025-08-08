ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT fk_rpfvr_cuc 
    FOREIGN KEY (qty_produced_crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT fk_rpfvr_fvtc 
    FOREIGN KEY (fruit_veg_type_code)
    REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code)
;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD CONSTRAINT fk_rpfvr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
