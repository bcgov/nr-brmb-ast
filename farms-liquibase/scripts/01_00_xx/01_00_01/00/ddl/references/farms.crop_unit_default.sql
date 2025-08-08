ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT fk_cud_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT fk_cud_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;
