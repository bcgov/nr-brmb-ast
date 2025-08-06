ALTER TABLE farms.farm_scenario_config_params ADD CONSTRAINT fk_scp_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
