ALTER TABLE farms.z42_participant_reference_year ADD CONSTRAINT fk_zpry_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.z03_statement_information(operation_number, participant_pin, program_year)
;
