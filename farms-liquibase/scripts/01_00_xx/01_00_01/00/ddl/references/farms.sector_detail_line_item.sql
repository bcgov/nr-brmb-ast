ALTER TABLE farms.sector_detail_line_item ADD CONSTRAINT fk_sdli_sdc 
    FOREIGN KEY (sector_detail_code)
    REFERENCES farms.sector_detail_code(sector_detail_code)
;
