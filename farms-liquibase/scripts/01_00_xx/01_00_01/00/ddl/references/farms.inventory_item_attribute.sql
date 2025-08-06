ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT fk_iia_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT fk_iia_iic1 
    FOREIGN KEY (rollup_inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;
