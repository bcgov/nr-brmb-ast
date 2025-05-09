ALTER TABLE z21_participant_supplementary ADD CONSTRAINT fk_zps_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
