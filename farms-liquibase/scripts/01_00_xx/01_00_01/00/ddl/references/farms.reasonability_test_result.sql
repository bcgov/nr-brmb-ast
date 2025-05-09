ALTER TABLE farms.reasonability_test_result ADD CONSTRAINT fk_rtr_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
