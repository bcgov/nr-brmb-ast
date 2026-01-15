ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT farm_rbrpu_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT farm_rbrpu_farm_rtr_fk FOREIGN KEY (reasonability_test_result_id) REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT farm_rbrpu_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
