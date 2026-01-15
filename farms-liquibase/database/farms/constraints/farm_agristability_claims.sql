ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_farm_scmc_exp_fk FOREIGN KEY (expense_structural_change_code) REFERENCES farms.farm_structural_change_codes(structural_change_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_farm_scmc_fk FOREIGN KEY (structural_change_code) REFERENCES farms.farm_structural_change_codes(structural_change_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
