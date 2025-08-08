ALTER TABLE farms.farm_z42_participant_ref_years ADD CONSTRAINT fk_zpry_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
