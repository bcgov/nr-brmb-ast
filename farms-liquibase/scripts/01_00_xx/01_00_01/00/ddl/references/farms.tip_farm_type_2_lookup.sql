ALTER TABLE farms.farm_tip_farm_type_2_lookups ADD CONSTRAINT fk_tft2l_tft3l 
    FOREIGN KEY (tip_farm_type_3_lookup_id)
    REFERENCES farms.farm_tip_farm_type_3_lookups(tip_farm_type_3_lookup_id)
;
