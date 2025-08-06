ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT fk_sdx_sc 
    FOREIGN KEY (sector_code)
    REFERENCES farms.farm_sector_codes(sector_code)
;

ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT fk_sdx_sdc 
    FOREIGN KEY (sector_detail_code)
    REFERENCES farms.farm_sector_detail_codes(sector_detail_code)
;
