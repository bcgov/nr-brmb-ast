ALTER TABLE farming_operation ADD CONSTRAINT fk_fo_fac 
    FOREIGN KEY (federal_accounting_code)
    REFERENCES federal_accounting_code(federal_accounting_code)
;

ALTER TABLE farming_operation ADD CONSTRAINT fk_fo_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES program_year_version(program_year_version_id)
;
