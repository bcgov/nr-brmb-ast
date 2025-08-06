ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT fk_bct_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
