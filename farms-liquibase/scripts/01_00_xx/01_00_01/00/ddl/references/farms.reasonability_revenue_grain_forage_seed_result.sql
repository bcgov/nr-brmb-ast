ALTER TABLE reasonability_revenue_grain_forage_seed_result ADD CONSTRAINT fk_rrgfsr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
