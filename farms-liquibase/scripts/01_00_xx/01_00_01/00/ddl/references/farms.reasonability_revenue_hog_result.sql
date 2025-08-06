ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT fk_rrhr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
