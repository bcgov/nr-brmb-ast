ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT fk_cucf_cuc 
    FOREIGN KEY (target_crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT fk_cucf_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;
