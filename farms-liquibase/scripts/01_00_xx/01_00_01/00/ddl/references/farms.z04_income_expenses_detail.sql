ALTER TABLE farms.farm_z04_income_exps_dtls ADD CONSTRAINT fk_zied_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
