ALTER TABLE farms.farm_scenario_state_audits ADD CONSTRAINT farm_ssa_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_scenario_state_audits ADD CONSTRAINT farm_ssa_farm_ssc_fk FOREIGN KEY (scenario_state_code) REFERENCES farms.farm_scenario_state_codes(scenario_state_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
