ALTER TABLE farms.agristabilty_commodity_xref ADD CONSTRAINT fk_acx_icc 
    FOREIGN KEY (inventory_class_code)
    REFERENCES farms.inventory_class_code(inventory_class_code)
;

ALTER TABLE farms.agristabilty_commodity_xref ADD CONSTRAINT fk_acx_igc 
    FOREIGN KEY (inventory_group_code)
    REFERENCES farms.inventory_group_code(inventory_group_code)
;

ALTER TABLE farms.agristabilty_commodity_xref ADD CONSTRAINT fk_acx_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;
