ALTER TABLE z04_income_expenses_detail ADD CONSTRAINT fk_zied_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES z03_statement_information(operation_number, participant_pin, program_year)
;
