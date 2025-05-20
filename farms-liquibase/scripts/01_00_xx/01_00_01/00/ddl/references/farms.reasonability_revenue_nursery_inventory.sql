ALTER TABLE farms.reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.reasonability_revenue_nursery_inventory ADD CONSTRAINT fk_rrni1_rrnr 
    FOREIGN KEY (reasonability_revenue_nursery_result_id)
    REFERENCES farms.reasonability_revenue_nursery_result(reasonability_revenue_nursery_result_id)
;
