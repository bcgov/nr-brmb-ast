ALTER TABLE reasonability_result_message ADD CONSTRAINT fk_rrm_mtc 
    FOREIGN KEY (message_type_code)
    REFERENCES message_type_code(message_type_code)
;

ALTER TABLE reasonability_result_message ADD CONSTRAINT fk_rrm_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;
