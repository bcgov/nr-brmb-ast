ALTER TABLE farms.crop_unit_default ADD CONSTRAINT fk_cud_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.crop_unit_default ADD CONSTRAINT fk_cud_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;
