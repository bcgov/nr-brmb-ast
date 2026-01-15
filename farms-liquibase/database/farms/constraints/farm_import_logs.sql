ALTER TABLE farms.farm_import_logs ADD CONSTRAINT farm_ilg_farm_iv_fk FOREIGN KEY (import_version_id) REFERENCES farms.farm_import_versions(import_version_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_import_logs ADD CONSTRAINT farm_ilg_farm_pyv_fk FOREIGN KEY (program_year_version_id) REFERENCES farms.farm_program_year_versions(program_year_version_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
