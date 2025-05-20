ALTER TABLE farms.whole_farm_participant ADD CONSTRAINT fk_wfp_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.program_year_version(program_year_version_id)
;
