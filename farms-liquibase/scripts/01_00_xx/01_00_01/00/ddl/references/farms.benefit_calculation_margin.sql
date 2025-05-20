ALTER TABLE farms.benefit_calculation_margin ADD CONSTRAINT fk_bcm_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.benefit_calculation_margin ADD CONSTRAINT fk_bcm_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farming_operation(farming_operation_id)
;
