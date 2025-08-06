ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT fk_pyv_fsc 
    FOREIGN KEY (federal_status_code)
    REFERENCES farms.farm_federal_status_codes(federal_status_code)
;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT fk_pyv_iv 
    FOREIGN KEY (import_version_id)
    REFERENCES farms.farm_import_versions(import_version_id)
;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT fk_pyv_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.farm_municipality_codes(municipality_code)
;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT fk_pyv_ppc 
    FOREIGN KEY (participant_profile_code)
    REFERENCES farms.farm_participant_profile_codes(participant_profile_code)
;

ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT fk_pyv_py 
    FOREIGN KEY (program_year_id)
    REFERENCES farms.farm_program_years(program_year_id)
;
