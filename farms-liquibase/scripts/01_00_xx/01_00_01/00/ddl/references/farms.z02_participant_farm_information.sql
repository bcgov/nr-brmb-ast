ALTER TABLE farms.farm_z02_partpnt_farm_infos ADD CONSTRAINT fk_zpfi_zpi 
    FOREIGN KEY (participant_pin)
    REFERENCES farms.farm_z01_participant_infos(participant_pin)
;
