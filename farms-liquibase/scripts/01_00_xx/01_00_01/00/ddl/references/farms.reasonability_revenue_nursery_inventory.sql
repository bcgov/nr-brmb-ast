ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT fk_rrni1_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT fk_rrni1_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_rsn_rev_nursery_invntries ADD CONSTRAINT fk_rrni1_rrnr 
    FOREIGN KEY (rsn_rev_nursery_result_id)
    REFERENCES farms.farm_rsn_rev_nursery_results(rsn_rev_nursery_result_id)
;
