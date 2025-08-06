ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT fk_tir_tft1l 
    FOREIGN KEY (tip_farm_type_1_lookup_id)
    REFERENCES farms.farm_tip_farm_type_1_lookups(tip_farm_type_1_lookup_id)
;

ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT fk_tir_tft2l 
    FOREIGN KEY (tip_farm_type_2_lookup_id)
    REFERENCES farms.farm_tip_farm_type_2_lookups(tip_farm_type_2_lookup_id)
;

ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT fk_tir_tft3l 
    FOREIGN KEY (tip_farm_type_3_lookup_id)
    REFERENCES farms.farm_tip_farm_type_3_lookups(tip_farm_type_3_lookup_id)
;
