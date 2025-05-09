ALTER TABLE fair_market_value ADD CONSTRAINT fk_fmv_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE fair_market_value ADD CONSTRAINT fk_fmv_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE fair_market_value ADD CONSTRAINT fk_fmv_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES municipality_code(municipality_code)
;
