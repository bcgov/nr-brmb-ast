ALTER TABLE tip_farm_type_2_lookup ADD CONSTRAINT fk_tft2l_tft3l 
    FOREIGN KEY (tip_farm_type_3_lookup_id)
    REFERENCES tip_farm_type_3_lookup(tip_farm_type_3_lookup_id)
;
