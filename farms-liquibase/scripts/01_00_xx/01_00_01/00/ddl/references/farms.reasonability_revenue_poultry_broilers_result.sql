ALTER TABLE reasonability_revenue_poultry_broilers_result ADD CONSTRAINT fk_rrpbr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
