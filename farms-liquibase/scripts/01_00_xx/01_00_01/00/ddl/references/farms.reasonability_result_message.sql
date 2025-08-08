ALTER TABLE farms.farm_rsn_result_messages ADD CONSTRAINT fk_rrm_mtc 
    FOREIGN KEY (message_type_code)
    REFERENCES farms.farm_message_type_codes(message_type_code)
;

ALTER TABLE farms.farm_rsn_result_messages ADD CONSTRAINT fk_rrm_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
