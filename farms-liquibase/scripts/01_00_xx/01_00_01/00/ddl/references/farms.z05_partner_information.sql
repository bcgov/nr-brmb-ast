ALTER TABLE farms.farm_z05_partner_infos ADD CONSTRAINT fk_zpi_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
