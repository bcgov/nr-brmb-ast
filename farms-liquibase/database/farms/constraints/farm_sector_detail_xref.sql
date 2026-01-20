ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT farm_sdx_farm_sc_fk FOREIGN KEY (sector_code) REFERENCES farms.farm_sector_codes(sector_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT farm_sdx_farm_sdc_fk FOREIGN KEY (sector_detail_code) REFERENCES farms.farm_sector_detail_codes(sector_detail_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
