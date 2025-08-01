ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_fsc_fk FOREIGN KEY (federal_status_code) REFERENCES farm_federal_status_codes(federal_status_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_mc_fk FOREIGN KEY (municipality_code) REFERENCES farm_municipality_codes(municipality_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_farm_ppc_fk FOREIGN KEY (participant_profile_code) REFERENCES farm_participant_profile_codes(participant_profile_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
