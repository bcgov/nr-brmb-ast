ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_farm_tft1l_fk FOREIGN KEY (tip_farm_type_1_lookup_id) REFERENCES farms.farm_tip_farm_type_1_lookups(tip_farm_type_1_lookup_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_farm_tft2l_fk FOREIGN KEY (tip_farm_type_2_lookup_id) REFERENCES farms.farm_tip_farm_type_2_lookups(tip_farm_type_2_lookup_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_farm_tft3l_fk FOREIGN KEY (tip_farm_type_3_lookup_id) REFERENCES farms.farm_tip_farm_type_3_lookups(tip_farm_type_3_lookup_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
