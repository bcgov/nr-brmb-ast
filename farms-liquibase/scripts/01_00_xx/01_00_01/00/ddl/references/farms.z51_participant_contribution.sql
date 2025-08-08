ALTER TABLE farms.farm_z51_participant_contribs ADD CONSTRAINT fk_zpc_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES farms.farm_z02_partpnt_farm_infos(participant_pin, program_year)
;
