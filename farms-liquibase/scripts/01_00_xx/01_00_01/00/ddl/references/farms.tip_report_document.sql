ALTER TABLE tip_report_document ADD CONSTRAINT fk_trd_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;

ALTER TABLE tip_report_document ADD CONSTRAINT fk_trd_py 
    FOREIGN KEY (program_year_id)
    REFERENCES program_year(program_year_id)
;
