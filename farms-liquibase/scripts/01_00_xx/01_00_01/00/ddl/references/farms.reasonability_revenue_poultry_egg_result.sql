ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT fk_rrper_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
