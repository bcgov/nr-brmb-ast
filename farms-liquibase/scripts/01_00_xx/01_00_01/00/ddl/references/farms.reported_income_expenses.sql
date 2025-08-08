ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT fk_rie_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT fk_rie_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;

ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT fk_rie_rie 
    FOREIGN KEY (cra_reported_income_expense_id)
    REFERENCES farms.farm_reported_income_expenses(reported_income_expense_id)
;
