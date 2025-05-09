ALTER TABLE program_year_version ADD CONSTRAINT fk_pyv_fsc 
    FOREIGN KEY (federal_status_code)
    REFERENCES federal_status_code(federal_status_code)
;

ALTER TABLE program_year_version ADD CONSTRAINT fk_pyv_iv 
    FOREIGN KEY (import_version_id)
    REFERENCES import_version(import_version_id)
;

ALTER TABLE program_year_version ADD CONSTRAINT fk_pyv_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES municipality_code(municipality_code)
;

ALTER TABLE program_year_version ADD CONSTRAINT fk_pyv_ppc 
    FOREIGN KEY (participant_profile_code)
    REFERENCES participant_profile_code(participant_profile_code)
;

ALTER TABLE program_year_version ADD CONSTRAINT fk_pyv_py 
    FOREIGN KEY (program_year_id)
    REFERENCES program_year(program_year_id)
;
