ALTER TABLE tip_farm_type_1_lookup ADD CONSTRAINT fk_tft1l_tft2l 
    FOREIGN KEY (tip_farm_type_2_lookup_id)
    REFERENCES tip_farm_type_2_lookup(tip_farm_type_2_lookup_id)
;
