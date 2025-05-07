ALTER TABLE z23_livestock_production_capacity ADD CONSTRAINT fk_zlpc_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
