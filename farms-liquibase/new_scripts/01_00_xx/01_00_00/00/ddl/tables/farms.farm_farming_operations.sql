ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT farm_fo_farm_aac_fk FOREIGN KEY (federal_accounting_code) REFERENCES farm_federal_accounting_codes(federal_accounting_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT farm_fo_farm_pyv_fk FOREIGN KEY (program_year_version_id) REFERENCES farm_program_year_versions(program_year_version_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
