ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_pdsc_fk FOREIGN KEY (participnt_data_src_code) REFERENCES farms.farm_participnt_data_src_codes(participnt_data_src_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_pyv_fk FOREIGN KEY (program_year_version_id) REFERENCES farms.farm_program_year_versions(program_year_version_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_scc_fk FOREIGN KEY (scenario_category_code) REFERENCES farms.farm_scenario_category_codes(scenario_category_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_ssc_fk FOREIGN KEY (scenario_state_code) REFERENCES farms.farm_scenario_state_codes(scenario_state_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_stc_fk FOREIGN KEY (scenario_class_code) REFERENCES farms.farm_scenario_class_codes(scenario_class_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_farm_tqc_fk FOREIGN KEY (triage_queue_code) REFERENCES farms.farm_triage_queue_codes(triage_queue_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
