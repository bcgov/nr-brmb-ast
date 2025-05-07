ALTER TABLE office_municipality_xref ADD CONSTRAINT fk_omx_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES municipality_code(municipality_code)
;

ALTER TABLE office_municipality_xref ADD CONSTRAINT fk_omx_roc 
    FOREIGN KEY (regional_office_code)
    REFERENCES regional_office_code(regional_office_code)
;
