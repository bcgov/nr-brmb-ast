ALTER TABLE benefit_calculation_total ADD CONSTRAINT fk_bct_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
