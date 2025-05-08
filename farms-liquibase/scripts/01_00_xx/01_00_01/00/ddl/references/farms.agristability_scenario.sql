ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_cs 
    FOREIGN KEY (chef_submission_id)
    REFERENCES chef_submission(chef_submission_id)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_pdsc 
    FOREIGN KEY (participant_data_source_code)
    REFERENCES participant_data_source_code(participant_data_source_code)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES program_year_version(program_year_version_id)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_scc 
    FOREIGN KEY (scenario_category_code)
    REFERENCES scenario_category_code(scenario_category_code)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_scc1 
    FOREIGN KEY (scenario_class_code)
    REFERENCES scenario_class_code(scenario_class_code)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES scenario_state_code(scenario_state_code)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_tqc 
    FOREIGN KEY (triage_queue_code)
    REFERENCES triage_queue_code(triage_queue_code)
;

ALTER TABLE agristability_scenario ADD CONSTRAINT fk_as_u 
    FOREIGN KEY (verifier_user_id)
    REFERENCES "user"(user_id)
;
