ALTER TABLE farms.farm_fruit_veg_type_details ADD CONSTRAINT fk_fvtd_fvtc 
    FOREIGN KEY (fruit_veg_type_code)
    REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code)
;
