ALTER TABLE farms.reasonability_revenue_hog_result ADD CONSTRAINT fk_rrhr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;
