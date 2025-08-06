ALTER TABLE farms.farm_z22_production_insurances ADD CONSTRAINT fk_zpi_zsi1 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
