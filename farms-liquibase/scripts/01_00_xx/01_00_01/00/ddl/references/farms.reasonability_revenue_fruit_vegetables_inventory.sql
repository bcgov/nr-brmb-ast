ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD CONSTRAINT fk_rrfvi_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD CONSTRAINT fk_rrfvi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD CONSTRAINT fk_rrfvi_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.farm_reasonabilty_test_results(reasonability_test_result_id)
;
