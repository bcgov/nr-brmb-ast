ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT fk_rs_as 
    FOREIGN KEY (for_agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT fk_rs_as1 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
