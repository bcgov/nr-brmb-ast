ALTER TABLE farms.farm_z50_participnt_bnft_calcs ADD CONSTRAINT fk_zpbc_zpfi 
    FOREIGN KEY (participant_pin, program_year)
    REFERENCES farms.farm_z02_partpnt_farm_infos(participant_pin, program_year)
;
