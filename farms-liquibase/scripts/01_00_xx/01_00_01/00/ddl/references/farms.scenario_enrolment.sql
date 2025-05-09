ALTER TABLE scenario_enrolment ADD CONSTRAINT fk_se_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE scenario_enrolment ADD CONSTRAINT fk_se_ectc 
    FOREIGN KEY (enrolment_calculation_type_code)
    REFERENCES enrolment_calculation_type_code(enrolment_calculation_type_code)
;
