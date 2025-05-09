ALTER TABLE expected_production ADD CONSTRAINT fk_ep_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE expected_production ADD CONSTRAINT fk_ep_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;
