ALTER TABLE reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
