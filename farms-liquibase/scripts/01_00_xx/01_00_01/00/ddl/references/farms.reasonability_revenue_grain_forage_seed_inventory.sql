ALTER TABLE farms.reasonability_revenue_grain_forage_seed_inventory ADD CONSTRAINT fk_rrgfsi_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.reasonability_revenue_grain_forage_seed_inventory ADD CONSTRAINT fk_rrgfsi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.reasonability_revenue_grain_forage_seed_inventory ADD CONSTRAINT fk_rrgfsi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;
