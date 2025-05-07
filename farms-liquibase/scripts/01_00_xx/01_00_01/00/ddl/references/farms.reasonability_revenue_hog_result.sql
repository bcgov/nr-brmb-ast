ALTER TABLE reasonability_revenue_hog_result ADD CONSTRAINT fk_rrhr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
