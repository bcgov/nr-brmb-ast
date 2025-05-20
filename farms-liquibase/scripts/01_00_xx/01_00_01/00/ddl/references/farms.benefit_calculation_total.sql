ALTER TABLE farms.benefit_calculation_total ADD CONSTRAINT fk_bct_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
