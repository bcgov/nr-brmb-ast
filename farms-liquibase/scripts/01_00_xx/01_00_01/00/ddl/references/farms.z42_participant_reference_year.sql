ALTER TABLE z42_participant_reference_year ADD CONSTRAINT fk_zpry_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
