ALTER TABLE farms.farm_import_logs ADD CONSTRAINT fk_il_iv 
    FOREIGN KEY (import_version_id)
    REFERENCES farms.farm_import_versions(import_version_id)
;

ALTER TABLE farms.farm_import_logs ADD CONSTRAINT fk_il_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.farm_program_year_versions(program_year_version_id)
;
