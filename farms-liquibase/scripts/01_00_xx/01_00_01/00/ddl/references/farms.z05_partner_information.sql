ALTER TABLE z05_partner_information ADD CONSTRAINT fk_zpi_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
