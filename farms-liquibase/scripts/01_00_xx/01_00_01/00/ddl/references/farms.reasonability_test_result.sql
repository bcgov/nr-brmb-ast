ALTER TABLE reasonability_test_result ADD CONSTRAINT fk_rtr_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
