ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT fk_ac_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT fk_ac_scc 
    FOREIGN KEY (expense_structural_change_code)
    REFERENCES farms.farm_structural_change_codes(structural_change_code)
;

ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT fk_ac_scc1 
    FOREIGN KEY (structural_change_code)
    REFERENCES farms.farm_structural_change_codes(structural_change_code)
;
