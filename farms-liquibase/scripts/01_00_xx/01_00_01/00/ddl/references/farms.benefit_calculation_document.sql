ALTER TABLE benefit_calculation_document ADD CONSTRAINT fk_bcd_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
