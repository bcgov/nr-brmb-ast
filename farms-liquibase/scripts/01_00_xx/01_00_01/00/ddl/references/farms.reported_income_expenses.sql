ALTER TABLE farms.reported_income_expenses ADD CONSTRAINT fk_rie_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.reported_income_expenses ADD CONSTRAINT fk_rie_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farming_operation(farming_operation_id)
;

ALTER TABLE farms.reported_income_expenses ADD CONSTRAINT fk_rie_rie 
    FOREIGN KEY (cra_reported_income_expense_id)
    REFERENCES farms.reported_income_expenses(reported_income_expense_id)
;
