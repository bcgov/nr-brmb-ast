ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_fsc_fk FOREIGN KEY (federal_status_code) REFERENCES farms.farm_federal_status_codes(federal_status_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_iv_fk FOREIGN KEY (import_version_id) REFERENCES farms.farm_import_versions(import_version_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farms.farm_municipality_codes(municipality_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_ppc_fk FOREIGN KEY (participant_profile_code) REFERENCES farms.farm_participant_profile_codes(participant_profile_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_py_fk FOREIGN KEY (program_year_id) REFERENCES farms.farm_program_years(program_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
