ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT fk_se_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT fk_se_ectc 
    FOREIGN KEY (enrolment_calc_type_code)
    REFERENCES farms.farm_enrolment_calc_type_codes(enrolment_calc_type_code)
;
