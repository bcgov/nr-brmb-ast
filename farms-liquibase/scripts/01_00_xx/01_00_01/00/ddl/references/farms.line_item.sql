ALTER TABLE farms.farm_line_items ADD CONSTRAINT fk_cli_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES farms.farm_commodity_type_codes(commodity_type_code)
;

ALTER TABLE farms.farm_line_items ADD CONSTRAINT fk_li_fvtc 
    FOREIGN KEY (fruit_veg_type_code)
    REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code)
;
