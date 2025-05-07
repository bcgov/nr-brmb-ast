ALTER TABLE reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_rrnr 
    FOREIGN KEY (reasonability_revenue_nursery_result_id)
    REFERENCES reasonability_revenue_nursery_result(reasonability_revenue_nursery_result_id)
;
