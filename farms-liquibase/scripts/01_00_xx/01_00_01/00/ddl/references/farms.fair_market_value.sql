ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT fk_fmv_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT fk_fmv_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT fk_fmv_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.farm_municipality_codes(municipality_code)
;
