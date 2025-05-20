ALTER TABLE farms.scenario_log ADD CONSTRAINT fk_sl_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
