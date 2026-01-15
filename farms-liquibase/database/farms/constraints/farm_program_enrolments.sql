ALTER TABLE farms.farm_program_enrolments ADD CONSTRAINT farm_pgre_farm_asc_fk FOREIGN KEY (agristability_client_id) REFERENCES farms.farm_agristability_clients(agristability_client_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_program_enrolments ADD CONSTRAINT farm_pgre_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
