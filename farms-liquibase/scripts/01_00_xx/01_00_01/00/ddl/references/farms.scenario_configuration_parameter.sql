ALTER TABLE farms.scenario_configuration_parameter ADD CONSTRAINT fk_scp_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
