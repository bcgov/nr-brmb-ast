ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT fk_acx_icc 
    FOREIGN KEY (inventory_class_code)
    REFERENCES farms.farm_inventory_class_codes(inventory_class_code)
;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT fk_acx_igc 
    FOREIGN KEY (inventory_group_code)
    REFERENCES farms.farm_inventory_group_codes(inventory_group_code)
;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT fk_acx_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;
