ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT fk_rrhi_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT fk_rrhi_rrhr 
    FOREIGN KEY (rsn_rev_hog_result_id)
    REFERENCES farms.farm_rsn_rev_hog_results(rsn_rev_hog_result_id)
;
