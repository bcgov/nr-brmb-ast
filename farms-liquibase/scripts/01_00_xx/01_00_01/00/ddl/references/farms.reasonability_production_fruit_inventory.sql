ALTER TABLE reasonability_production_fruit_inventory ADD CONSTRAINT fk_rpfi_cuc 
    FOREIGN KEY (quantity_produced_crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reasonability_production_fruit_inventory ADD CONSTRAINT fk_rpfi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_production_fruit_inventory ADD CONSTRAINT fk_rpfi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
