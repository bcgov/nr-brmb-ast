ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT farm_rie_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT farm_rie_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT farm_rie_farm_rie_fk FOREIGN KEY (cra_reported_income_expense_id) REFERENCES farms.farm_reported_income_expenses(reported_income_expense_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
