ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT fk_rfc_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;

ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT fk_rfc_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;
