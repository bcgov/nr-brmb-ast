ALTER TABLE farms.farm_program_years ADD CONSTRAINT fk_py_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.farm_agristability_clients(agristability_client_id)
;

ALTER TABLE farms.farm_program_years ADD CONSTRAINT fk_py_ftc 
    FOREIGN KEY (farm_type_code)
    REFERENCES farms.farm_farm_type_codes(farm_type_code)
;
