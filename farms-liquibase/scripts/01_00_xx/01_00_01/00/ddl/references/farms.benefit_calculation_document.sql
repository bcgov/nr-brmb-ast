ALTER TABLE farms.farm_benefit_calc_documents ADD CONSTRAINT fk_bcd_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
