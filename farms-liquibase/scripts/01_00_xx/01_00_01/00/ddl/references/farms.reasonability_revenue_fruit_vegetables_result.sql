ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT fk_rrfvr_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT fk_rrfvr_fvtc 
    FOREIGN KEY (fruit_veg_type_code)
    REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code)
;

ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT fk_rrfvr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
