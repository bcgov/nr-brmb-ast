ALTER TABLE farms.farm_rsn_rev_g_f_fs_invntories ADD CONSTRAINT fk_rrgfsi_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_rsn_rev_g_f_fs_invntories ADD CONSTRAINT fk_rrgfsi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_rsn_rev_g_f_fs_invntories ADD CONSTRAINT fk_rrgfsi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
