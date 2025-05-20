ALTER TABLE farms.reasonability_revenue_nursery_income ADD CONSTRAINT fk_rrni_rrnr 
    FOREIGN KEY (reasonability_revenue_nursery_result_id)
    REFERENCES farms.reasonability_revenue_nursery_result(reasonability_revenue_nursery_result_id)
;
