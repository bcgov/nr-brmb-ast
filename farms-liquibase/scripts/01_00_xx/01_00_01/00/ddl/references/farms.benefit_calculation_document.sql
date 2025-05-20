ALTER TABLE farms.benefit_calculation_document ADD CONSTRAINT fk_bcd_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
