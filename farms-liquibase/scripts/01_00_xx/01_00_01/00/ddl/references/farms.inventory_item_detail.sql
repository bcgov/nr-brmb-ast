ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT fk_iid_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES farms.farm_commodity_type_codes(commodity_type_code)
;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT fk_iid_fvtc 
    FOREIGN KEY (fruit_veg_type_code)
    REFERENCES farms.farm_fruit_veg_type_codes(fruit_veg_type_code)
;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT fk_iid_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT fk_iid_mscc 
    FOREIGN KEY (multi_stage_commdty_code)
    REFERENCES farms.farm_multi_stage_commdty_codes(multi_stage_commdty_code)
;
