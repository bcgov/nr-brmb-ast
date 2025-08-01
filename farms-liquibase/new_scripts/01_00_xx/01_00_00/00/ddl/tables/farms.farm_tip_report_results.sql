ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_derr_fk FOREIGN KEY (direct_expenses_ratio_rating) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_lbrr_fk FOREIGN KEY (land_build_expens_ratio_rating) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_merr_fk FOREIGN KEY (machinery_expenses_ratio_ratng) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_ocrr_fk FOREIGN KEY (operating_cost_ratio_rating) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_oerr_fk FOREIGN KEY (overhead_expenses_ratio_rating) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_farm_trc_pmrr_fk FOREIGN KEY (production_margin_ratio_rating) REFERENCES farm_tip_rating_codes(tip_rating_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
