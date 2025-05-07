ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_py 
    FOREIGN KEY (program_year_id)
    REFERENCES program_year(program_year_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_tby 
    FOREIGN KEY (tip_benchmark_year_id)
    REFERENCES tip_benchmark_year(tip_benchmark_year_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_tby1 
    FOREIGN KEY (tip_benchmark_year_id_type_1)
    REFERENCES tip_benchmark_year(tip_benchmark_year_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_tby2 
    FOREIGN KEY (tip_benchmark_year_id_type_2)
    REFERENCES tip_benchmark_year(tip_benchmark_year_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_tby3 
    FOREIGN KEY (tip_benchmark_year_id_type_3)
    REFERENCES tip_benchmark_year(tip_benchmark_year_id)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc 
    FOREIGN KEY (direct_expenses_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc1 
    FOREIGN KEY (land_build_expenses_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc2 
    FOREIGN KEY (machinery_expenses_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc3 
    FOREIGN KEY (operating_cost_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc4 
    FOREIGN KEY (overhead_expenses_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trc5 
    FOREIGN KEY (production_margin_ratio_rating)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_result ADD CONSTRAINT fk_trr_trr 
    FOREIGN KEY (parent_tip_report_result_id)
    REFERENCES tip_report_result(tip_report_result_id)
;
