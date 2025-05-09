ALTER TABLE farms.tip_report_expense ADD CONSTRAINT fk_tre_trc 
    FOREIGN KEY (tip_rating_code)
    REFERENCES farms.tip_rating_code(tip_rating_code)
;

ALTER TABLE farms.tip_report_expense ADD CONSTRAINT fk_tre_trr 
    FOREIGN KEY (tip_report_result_id)
    REFERENCES farms.tip_report_result(tip_report_result_id)
;
