ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_cs 
    FOREIGN KEY (chef_submission_id)
    REFERENCES farms.chef_submission(chef_submission_id)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_pdsc 
    FOREIGN KEY (participant_data_source_code)
    REFERENCES farms.participant_data_source_code(participant_data_source_code)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_pyv 
    FOREIGN KEY (program_year_version_id)
    REFERENCES farms.program_year_version(program_year_version_id)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_scc 
    FOREIGN KEY (scenario_category_code)
    REFERENCES farms.scenario_category_code(scenario_category_code)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_scc1 
    FOREIGN KEY (scenario_class_code)
    REFERENCES farms.scenario_class_code(scenario_class_code)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_ssc 
    FOREIGN KEY (scenario_state_code)
    REFERENCES farms.scenario_state_code(scenario_state_code)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_tqc 
    FOREIGN KEY (triage_queue_code)
    REFERENCES farms.triage_queue_code(triage_queue_code)
;

ALTER TABLE farms.agristability_scenario ADD CONSTRAINT fk_as_u 
    FOREIGN KEY (verifier_user_id)
    REFERENCES farms.usr(user_id)
;
