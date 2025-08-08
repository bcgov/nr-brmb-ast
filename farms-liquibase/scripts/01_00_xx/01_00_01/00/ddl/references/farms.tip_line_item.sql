ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT fk_tli_tft1l 
    FOREIGN KEY (tip_farm_type_1_lookup_id)
    REFERENCES farms.farm_tip_farm_type_1_lookups(tip_farm_type_1_lookup_id)
;
