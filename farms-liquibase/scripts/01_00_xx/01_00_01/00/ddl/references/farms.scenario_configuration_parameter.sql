ALTER TABLE scenario_configuration_parameter ADD CONSTRAINT fk_scp_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
