ALTER TABLE import_log ADD CONSTRAINT fk_il_iv 
    FOREIGN KEY (import_version_id)
    REFERENCES import_version(import_version_id)
;

ALTER TABLE import_log ADD CONSTRAINT fk_il_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES program_year_version(program_year_version_id)
;
