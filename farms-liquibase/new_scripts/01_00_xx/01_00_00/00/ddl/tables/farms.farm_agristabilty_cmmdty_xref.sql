ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_icc_fk FOREIGN KEY (inventory_class_code) REFERENCES farms.farm_inventory_class_codes(inventory_class_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_farm_igc_fk FOREIGN KEY (inventory_group_code) REFERENCES farms.farm_inventory_group_codes(inventory_group_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
