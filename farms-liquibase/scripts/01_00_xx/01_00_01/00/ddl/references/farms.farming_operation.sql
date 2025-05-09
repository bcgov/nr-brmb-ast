ALTER TABLE farms.farming_operation ADD CONSTRAINT fk_fo_fac 
    FOREIGN KEY (federal_accounting_code)
    REFERENCES farms.federal_accounting_code(federal_accounting_code)
;

ALTER TABLE farms.farming_operation ADD CONSTRAINT fk_fo_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.program_year_version(program_year_version_id)
;
