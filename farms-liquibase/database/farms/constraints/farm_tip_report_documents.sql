ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT farm_trd_farm_fo_fk FOREIGN KEY (farming_operation_id) REFERENCES farms.farm_farming_operations(farming_operation_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT farm_trd_farm_py_fk FOREIGN KEY (program_year_id) REFERENCES farms.farm_program_years(program_year_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
