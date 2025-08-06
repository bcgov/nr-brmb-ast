ALTER TABLE farms.farm_z23_livestock_prod_cpcts ADD CONSTRAINT fk_zlpc_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
