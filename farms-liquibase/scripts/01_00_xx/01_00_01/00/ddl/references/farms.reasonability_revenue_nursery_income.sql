ALTER TABLE farms.farm_rsn_rev_nursery_incomes ADD CONSTRAINT fk_rrni_rrnr 
    FOREIGN KEY (rsn_rev_nursery_result_id)
    REFERENCES farms.farm_rsn_rev_nursery_results(rsn_rev_nursery_result_id)
;
