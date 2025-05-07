ALTER TABLE tip_line_item ADD CONSTRAINT fk_tli_tft1l 
    FOREIGN KEY (tip_farm_type_1_lookup_id)
    REFERENCES tip_farm_type_1_lookup(tip_farm_type_1_lookup_id)
;
