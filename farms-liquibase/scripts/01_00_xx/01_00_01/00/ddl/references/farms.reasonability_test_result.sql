ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT fk_rtr_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
