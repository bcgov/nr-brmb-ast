ALTER TABLE farms.z02_participant_farm_information ADD CONSTRAINT fk_zpfi_zpi 
    FOREIGN KEY (participant_pin)
    REFERENCES farms.z01_participant_information(participant_pin)
;
