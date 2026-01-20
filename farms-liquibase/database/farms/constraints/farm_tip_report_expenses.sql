ALTER TABLE farms.farm_tip_report_expenses ADD CONSTRAINT farm_tre_farm_trc_fk FOREIGN KEY (tip_rating_code) REFERENCES farms.farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_expenses ADD CONSTRAINT farm_tre_farm_trr_fk FOREIGN KEY (tip_report_result_id) REFERENCES farms.farm_tip_report_results(tip_report_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
