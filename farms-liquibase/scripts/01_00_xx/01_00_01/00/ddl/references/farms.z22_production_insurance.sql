ALTER TABLE farms.z22_production_insurance ADD CONSTRAINT fk_zpi_zsi1 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.z03_statement_information(operation_number, participant_pin, program_year)
;
