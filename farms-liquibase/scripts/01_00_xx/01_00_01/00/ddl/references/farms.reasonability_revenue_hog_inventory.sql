ALTER TABLE reasonability_revenue_hog_inventory ADD CONSTRAINT fk_rrhi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_revenue_hog_inventory ADD CONSTRAINT fk_rrhi_rrhr 
    FOREIGN KEY (reasonability_revenue_hog_result_id)
    REFERENCES reasonability_revenue_hog_result(reasonability_revenue_hog_result_id)
;
