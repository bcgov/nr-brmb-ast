ALTER TABLE scenario_log ADD CONSTRAINT fk_sl_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
