ALTER TABLE farms.farm_z03_statement_infos ADD CONSTRAINT fk_zsi_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES farms.farm_z02_partpnt_farm_infos(participant_pin, program_year)
;
