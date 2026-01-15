ALTER TABLE farms.farm_rsn_result_messages ADD CONSTRAINT farm_rrm_farm_mtc_fk FOREIGN KEY (message_type_code) REFERENCES farms.farm_message_type_codes(message_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_result_messages ADD CONSTRAINT farm_rrm_farm_rtr_fk FOREIGN KEY (reasonability_test_result_id) REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
