ALTER TABLE reasonability_revenue_nursery_result ADD CONSTRAINT fk_rrnr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
