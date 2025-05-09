ALTER TABLE agristability_claim ADD CONSTRAINT fk_ac_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE agristability_claim ADD CONSTRAINT fk_ac_scc 
    FOREIGN KEY (expense_structural_change_code)
    REFERENCES structural_change_code(structural_change_code)
;

ALTER TABLE agristability_claim ADD CONSTRAINT fk_ac_scc1 
    FOREIGN KEY (structural_change_code)
    REFERENCES structural_change_code(structural_change_code)
;
