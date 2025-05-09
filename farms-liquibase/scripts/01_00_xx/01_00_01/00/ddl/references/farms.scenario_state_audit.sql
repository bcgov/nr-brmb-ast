ALTER TABLE scenario_state_audit ADD CONSTRAINT fk_ssa_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE scenario_state_audit ADD CONSTRAINT fk_ssa_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES scenario_state_code(scenario_state_code)
;
