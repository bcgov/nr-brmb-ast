ALTER TABLE farms.scenario_enrolment ADD CONSTRAINT fk_se_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;

ALTER TABLE farms.scenario_enrolment ADD CONSTRAINT fk_se_ectc 
    FOREIGN KEY (enrolment_calculation_type_code)
    REFERENCES farms.enrolment_calculation_type_code(enrolment_calculation_type_code)
;
