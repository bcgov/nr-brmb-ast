ALTER TABLE farms.farm_office_municipality_xref ADD CONSTRAINT fk_omx_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.farm_municipality_codes(municipality_code)
;

ALTER TABLE farms.farm_office_municipality_xref ADD CONSTRAINT fk_omx_roc 
    FOREIGN KEY (regional_office_code)
    REFERENCES farms.farm_regional_office_codes(regional_office_code)
;
