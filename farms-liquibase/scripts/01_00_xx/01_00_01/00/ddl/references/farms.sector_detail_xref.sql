ALTER TABLE farms.sector_detail_xref ADD CONSTRAINT fk_sdx_sc 
    FOREIGN KEY (sector_code)
    REFERENCES farms.sector_code(sector_code)
;

ALTER TABLE farms.sector_detail_xref ADD CONSTRAINT fk_sdx_sdc 
    FOREIGN KEY (sector_detail_code)
    REFERENCES farms.sector_detail_code(sector_detail_code)
;
