ALTER TABLE farms.program_year ADD CONSTRAINT fk_py_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.agristability_client(agristability_client_id)
;

ALTER TABLE farms.program_year ADD CONSTRAINT fk_py_ftc 
    FOREIGN KEY (farm_type_code)
    REFERENCES farms.farm_type_code(farm_type_code)
;
