ALTER TABLE farms.z03_statement_information ADD CONSTRAINT fk_zsi_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES farms.z02_participant_farm_information(participant_pin, program_year)
;
