ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_farm_iic_fk FOREIGN KEY (inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_farm_riic_fk FOREIGN KEY (rollup_inventory_item_code) REFERENCES farms.farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
