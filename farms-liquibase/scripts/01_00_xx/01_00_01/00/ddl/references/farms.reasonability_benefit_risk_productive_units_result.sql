ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT fk_rbrpur_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT fk_rbrpur_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;

ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT fk_rbrpur_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;
