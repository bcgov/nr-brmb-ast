ALTER TABLE farms.scenario_state_audit ADD CONSTRAINT fk_ssa_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.scenario_state_audit ADD CONSTRAINT fk_ssa_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES farms.scenario_state_code(scenario_state_code)
;
