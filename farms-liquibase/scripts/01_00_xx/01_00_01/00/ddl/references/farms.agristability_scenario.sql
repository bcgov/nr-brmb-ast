ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_cs 
    FOREIGN KEY (chef_submission_id)
    REFERENCES farms.farm_chef_submissions(chef_submission_id)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_pdsc 
    FOREIGN KEY (participnt_data_src_code)
    REFERENCES farms.farm_participnt_data_src_codes(participnt_data_src_code)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.farm_program_year_versions(program_year_version_id)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_scc 
    FOREIGN KEY (scenario_category_code)
    REFERENCES farms.farm_scenario_category_codes(scenario_category_code)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_scc1 
    FOREIGN KEY (scenario_class_code)
    REFERENCES farms.farm_scenario_class_codes(scenario_class_code)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES farms.farm_scenario_state_codes(scenario_state_code)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_tqc 
    FOREIGN KEY (triage_queue_code)
    REFERENCES farms.farm_triage_queue_codes(triage_queue_code)
;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT fk_as_u 
    FOREIGN KEY (verifier_user_id)
    REFERENCES farms.farm_users(user_id)
;
