ALTER TABLE tip_income_range ADD CONSTRAINT fk_tir_tft1l 
    FOREIGN KEY (tip_farm_type_1_lookup_id)
    REFERENCES tip_farm_type_1_lookup(tip_farm_type_1_lookup_id)
;

ALTER TABLE tip_income_range ADD CONSTRAINT fk_tir_tft2l 
    FOREIGN KEY (tip_farm_type_2_lookup_id)
    REFERENCES tip_farm_type_2_lookup(tip_farm_type_2_lookup_id)
;

ALTER TABLE tip_income_range ADD CONSTRAINT fk_tir_tft3l 
    FOREIGN KEY (tip_farm_type_3_lookup_id)
    REFERENCES tip_farm_type_3_lookup(tip_farm_type_3_lookup_id)
;
