ALTER TABLE reasonability_forage_consumer ADD CONSTRAINT fk_rfc_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;

ALTER TABLE reasonability_forage_consumer ADD CONSTRAINT fk_rfc_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES structure_group_code(structure_group_code)
;
