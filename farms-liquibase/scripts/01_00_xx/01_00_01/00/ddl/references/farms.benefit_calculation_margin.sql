ALTER TABLE farms.farm_benefit_calc_margins ADD CONSTRAINT fk_bcm_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_benefit_calc_margins ADD CONSTRAINT fk_bcm_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;
