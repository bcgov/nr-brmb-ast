ALTER TABLE farms.reasonability_revenue_hog_inventory ADD CONSTRAINT fk_rrhi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.reasonability_revenue_hog_inventory ADD CONSTRAINT fk_rrhi_rrhr 
    FOREIGN KEY (reasonability_revenue_hog_result_id)
    REFERENCES farms.reasonability_revenue_hog_result(reasonability_revenue_hog_result_id)
;
