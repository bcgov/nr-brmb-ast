ALTER TABLE reasonability_production_grain_result ADD CONSTRAINT fk_rpgr_cuc 
    FOREIGN KEY (quantity_produced_crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reasonability_production_grain_result ADD CONSTRAINT fk_rpgr_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_production_grain_result ADD CONSTRAINT fk_rpgr_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
