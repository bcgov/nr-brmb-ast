ALTER TABLE tip_report_expense ADD CONSTRAINT fk_tre_trc 
    FOREIGN KEY (tip_rating_code)
    REFERENCES tip_rating_code(tip_rating_code)
;

ALTER TABLE tip_report_expense ADD CONSTRAINT fk_tre_trr 
    FOREIGN KEY (tip_report_result_id)
    REFERENCES tip_report_result(tip_report_result_id)
;
