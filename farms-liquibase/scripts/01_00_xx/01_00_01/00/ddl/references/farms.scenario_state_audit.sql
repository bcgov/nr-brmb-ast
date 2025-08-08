ALTER TABLE farms.farm_scenario_state_audits ADD CONSTRAINT fk_ssa_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_scenario_state_audits ADD CONSTRAINT fk_ssa_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES farms.farm_scenario_state_codes(scenario_state_code)
;
