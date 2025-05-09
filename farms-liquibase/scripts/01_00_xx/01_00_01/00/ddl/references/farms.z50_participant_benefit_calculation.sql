ALTER TABLE farms.z50_participant_benefit_calculation ADD CONSTRAINT fk_zpbc_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES farms.z02_participant_farm_information(participant_pin, program_year)
;
