ALTER TABLE farms.farm_sector_detail_line_items ADD CONSTRAINT fk_sdli_sdc 
    FOREIGN KEY (sector_detail_code)
    REFERENCES farms.farm_sector_detail_codes(sector_detail_code)
;
