ALTER TABLE farms.farm_office_municipality_xref ADD CONSTRAINT farm_omx_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_office_municipality_xref ADD CONSTRAINT farm_omx_farm_roc_fk FOREIGN KEY (regional_office_code) REFERENCES farms.farm_regional_office_codes(regional_office_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
