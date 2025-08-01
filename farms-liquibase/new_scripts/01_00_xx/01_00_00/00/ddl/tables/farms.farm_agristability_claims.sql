ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_farm_scmc_exp_fk FOREIGN KEY (expense_structural_change_code) REFERENCES farm_structural_change_codes(structural_change_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_farm_scmc_fk FOREIGN KEY (structural_change_code) REFERENCES farm_structural_change_codes(structural_change_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
