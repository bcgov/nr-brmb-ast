ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_farm_ctc_fk FOREIGN KEY (commodity_type_code) REFERENCES farms.farm_commodity_type_codes(commodity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_farm_fvtc_fk FOREIGN KEY (fruit_veg_type_code) REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
