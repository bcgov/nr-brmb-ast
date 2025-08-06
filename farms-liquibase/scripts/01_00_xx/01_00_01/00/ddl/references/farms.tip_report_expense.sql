ALTER TABLE farms.farm_tip_report_expenses ADD CONSTRAINT fk_tre_trc 
    FOREIGN KEY (tip_rating_code)
    REFERENCES farms.farm_tip_rating_codes(tip_rating_code)
;

ALTER TABLE farms.farm_tip_report_expenses ADD CONSTRAINT fk_tre_trr 
    FOREIGN KEY (tip_report_result_id)
    REFERENCES farms.farm_tip_report_results(tip_report_result_id)
;
