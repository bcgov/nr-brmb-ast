ALTER TABLE inventory_item_detail ADD CONSTRAINT fk_iid_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES commodity_type_code(commodity_type_code)
;

ALTER TABLE inventory_item_detail ADD CONSTRAINT fk_iid_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES fruit_vegetable_type_code(fruit_vegetable_type_code)
;

ALTER TABLE inventory_item_detail ADD CONSTRAINT fk_iid_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE inventory_item_detail ADD CONSTRAINT fk_iid_mscc 
    FOREIGN KEY (multplei_stage_commodity_code)
    REFERENCES multiple_stage_commodity_code(multplei_stage_commodity_code)
;
