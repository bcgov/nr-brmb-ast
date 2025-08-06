ALTER TABLE farms.farm_tip_farm_type_1_lookups ADD CONSTRAINT fk_tft1l_tft2l 
    FOREIGN KEY (tip_farm_type_2_lookup_id)
    REFERENCES farms.farm_tip_farm_type_2_lookups(tip_farm_type_2_lookup_id)
;
