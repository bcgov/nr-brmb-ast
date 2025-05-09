ALTER TABLE farms.reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.reasonability_revenue_fruit_vegetables_inventory ADD CONSTRAINT fk_rrfvi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;
