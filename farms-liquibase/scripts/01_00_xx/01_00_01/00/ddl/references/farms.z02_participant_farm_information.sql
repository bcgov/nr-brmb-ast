ALTER TABLE z02_participant_farm_information ADD CONSTRAINT fk_zpfi_zpi 
    FOREIGN KEY (participant_pin)
    REFERENCES z01_participant_information(participant_pin)
;
