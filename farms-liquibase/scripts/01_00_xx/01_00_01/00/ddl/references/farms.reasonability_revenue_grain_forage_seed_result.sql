ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ADD CONSTRAINT fk_rrgfsr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
