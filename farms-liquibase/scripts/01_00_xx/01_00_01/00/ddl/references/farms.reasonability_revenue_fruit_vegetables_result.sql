ALTER TABLE farms.reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES farms.fruit_vegetable_type_code(fruit_vegetable_type_code)
;

ALTER TABLE farms.reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;
