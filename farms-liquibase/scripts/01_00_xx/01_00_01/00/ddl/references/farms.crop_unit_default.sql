ALTER TABLE crop_unit_default ADD CONSTRAINT fk_cud_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE crop_unit_default ADD CONSTRAINT fk_cud_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;
