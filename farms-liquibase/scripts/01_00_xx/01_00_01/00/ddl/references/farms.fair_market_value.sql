ALTER TABLE farms.fair_market_value ADD CONSTRAINT fk_fmv_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.crop_unit_code(crop_unit_code)
;

ALTER TABLE farms.fair_market_value ADD CONSTRAINT fk_fmv_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.fair_market_value ADD CONSTRAINT fk_fmv_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.municipality_code(municipality_code)
;
