ALTER TABLE reported_income_expenses ADD CONSTRAINT fk_rie_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE reported_income_expenses ADD CONSTRAINT fk_rie_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;

ALTER TABLE reported_income_expenses ADD CONSTRAINT fk_rie_rie 
    FOREIGN KEY (cra_reported_income_expense_id)
    REFERENCES reported_income_expenses(reported_income_expense_id)
;
