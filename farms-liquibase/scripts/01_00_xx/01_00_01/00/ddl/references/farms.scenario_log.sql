ALTER TABLE farms.farm_scenario_logs ADD CONSTRAINT fk_sl_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
