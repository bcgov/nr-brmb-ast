ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT fk_trd_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;

ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT fk_trd_py 
    FOREIGN KEY (program_year_id)
    REFERENCES farms.farm_program_years(program_year_id)
;
