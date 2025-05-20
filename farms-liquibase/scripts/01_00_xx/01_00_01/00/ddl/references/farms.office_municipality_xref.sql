ALTER TABLE farms.office_municipality_xref ADD CONSTRAINT fk_omx_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.municipality_code(municipality_code)
;

ALTER TABLE farms.office_municipality_xref ADD CONSTRAINT fk_omx_roc 
    FOREIGN KEY (regional_office_code)
    REFERENCES farms.regional_office_code(regional_office_code)
;
