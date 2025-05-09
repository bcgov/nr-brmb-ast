ALTER TABLE farms.reasonability_revenue_nursery_result ADD CONSTRAINT fk_rrnr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;
