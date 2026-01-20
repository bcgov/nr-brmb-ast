ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT farm_rfc_farm_rtr_fk FOREIGN KEY (reasonability_test_result_id) REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT farm_rfc_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
