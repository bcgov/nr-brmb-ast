ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT farm_rs_farm_as_fk FOREIGN KEY (for_agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT farm_rthe_referencing_scena_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
