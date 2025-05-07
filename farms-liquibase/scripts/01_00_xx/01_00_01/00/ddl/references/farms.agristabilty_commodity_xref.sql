ALTER TABLE agristabilty_commodity_xref ADD CONSTRAINT fk_acx_icc 
    FOREIGN KEY (inventory_class_code)
    REFERENCES inventory_class_code(inventory_class_code)
;

ALTER TABLE agristabilty_commodity_xref ADD CONSTRAINT fk_acx_igc 
    FOREIGN KEY (inventory_group_code)
    REFERENCES inventory_group_code(inventory_group_code)
;

ALTER TABLE agristabilty_commodity_xref ADD CONSTRAINT fk_acx_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;
