ALTER TABLE z51_participant_contribution ADD CONSTRAINT fk_zpc_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES z02_participant_farm_information(participant_pin, program_year)
;
