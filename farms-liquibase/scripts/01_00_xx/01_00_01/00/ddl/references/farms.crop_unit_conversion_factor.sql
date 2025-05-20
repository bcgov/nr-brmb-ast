ALTER TABLE farms.crop_unit_conversion_factor ADD CONSTRAINT fk_cucf_cuc 
    FOREIGN KEY (target_crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.crop_unit_conversion_factor ADD CONSTRAINT fk_cucf_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;
