ALTER TABLE farms.reference_scenario ADD CONSTRAINT fk_rs_as 
    FOREIGN KEY (for_agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.reference_scenario ADD CONSTRAINT fk_rs_as1 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
