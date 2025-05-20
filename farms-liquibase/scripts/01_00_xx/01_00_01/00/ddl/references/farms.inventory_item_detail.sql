ALTER TABLE farms.inventory_item_detail ADD CONSTRAINT fk_iid_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES farms.commodity_type_code(commodity_type_code)
;

ALTER TABLE farms.inventory_item_detail ADD CONSTRAINT fk_iid_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES farms.fruit_vegetable_type_code(fruit_vegetable_type_code)
;

ALTER TABLE farms.inventory_item_detail ADD CONSTRAINT fk_iid_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.inventory_item_detail ADD CONSTRAINT fk_iid_mscc 
    FOREIGN KEY (multiple_stage_commodity_code)
    REFERENCES farms.multiple_stage_commodity_code(multiple_stage_commodity_code)
;
