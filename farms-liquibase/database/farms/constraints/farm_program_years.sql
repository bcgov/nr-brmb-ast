ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_farm_asc_fk FOREIGN KEY (agristability_client_id) REFERENCES farms.farm_agristability_clients(agristability_client_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_farm_ftc_fk FOREIGN KEY (farm_type_code) REFERENCES farms.farm_farm_type_codes(farm_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
