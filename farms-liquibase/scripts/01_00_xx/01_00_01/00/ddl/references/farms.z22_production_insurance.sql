ALTER TABLE z22_production_insurance ADD CONSTRAINT fk_zpi_zsi1 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
