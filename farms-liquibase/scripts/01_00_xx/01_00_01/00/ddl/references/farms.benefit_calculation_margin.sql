ALTER TABLE benefit_calculation_margin ADD CONSTRAINT fk_bcm_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE benefit_calculation_margin ADD CONSTRAINT fk_bcm_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;
