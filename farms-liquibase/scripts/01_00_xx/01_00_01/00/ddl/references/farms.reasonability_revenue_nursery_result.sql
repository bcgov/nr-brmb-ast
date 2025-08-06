ALTER TABLE farms.farm_rsn_rev_nursery_results ADD CONSTRAINT fk_rrnr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
