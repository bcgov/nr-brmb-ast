ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT fk_fo_fac 
    FOREIGN KEY (federal_accounting_code)
    REFERENCES farms.farm_federal_accounting_codes(federal_accounting_code)
;

ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT fk_fo_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.farm_program_year_versions(program_year_version_id)
;
