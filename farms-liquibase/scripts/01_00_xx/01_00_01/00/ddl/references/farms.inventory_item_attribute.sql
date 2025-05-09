ALTER TABLE farms.inventory_item_attribute ADD CONSTRAINT fk_iia_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.inventory_item_attribute ADD CONSTRAINT fk_iia_iic1 
    FOREIGN KEY (rollup_inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;
