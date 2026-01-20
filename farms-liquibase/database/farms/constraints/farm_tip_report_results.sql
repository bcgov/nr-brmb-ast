ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_py_fk FOREIGN KEY (program_year_id) REFERENCES farms.farm_program_years(program_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_tbyt1_fk FOREIGN KEY (tip_benchmark_year_id_type_1) REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_tbyt2_fk FOREIGN KEY (tip_benchmark_year_id_type_2) REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_tbyt3_fk FOREIGN KEY (tip_benchmark_year_id_type_3) REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_tby_fk FOREIGN KEY (tip_benchmark_year_id) REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_derr_fk FOREIGN KEY (direct_expenses_ratio_rating) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_lbrr_fk FOREIGN KEY (land_build_expens_ratio_rating) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_merr_fk FOREIGN KEY (machinery_expenses_ratio_ratng) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_ocrr_fk FOREIGN KEY (operating_cost_ratio_rating) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_oerr_fk FOREIGN KEY (overhead_expenses_ratio_rating) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_pmrr_fk FOREIGN KEY (production_margin_ratio_rating) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trr_fk FOREIGN KEY (parent_tip_report_result_id) REFERENCES farms.farm_tip_report_results(tip_report_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
