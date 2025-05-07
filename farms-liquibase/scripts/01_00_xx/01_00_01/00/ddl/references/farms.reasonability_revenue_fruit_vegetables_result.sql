ALTER TABLE reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES fruit_vegetable_type_code(fruit_vegetable_type_code)
;

ALTER TABLE reasonability_revenue_fruit_vegetables_result ADD CONSTRAINT fk_rrfvr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
