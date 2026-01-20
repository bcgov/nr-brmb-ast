ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_farm_as_fk FOREIGN KEY (agristability_scenario_id) REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_farm_ectc_fk FOREIGN KEY (enrolment_calc_type_code) REFERENCES farms.farm_enrolment_calc_type_codes(enrolment_calc_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
