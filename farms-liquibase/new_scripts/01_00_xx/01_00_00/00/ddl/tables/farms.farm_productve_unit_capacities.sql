ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_ic_fk FOREIGN KEY (inventory_item_code) REFERENCES farm_inventory_item_codes(inventory_item_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_pdsc_fk FOREIGN KEY (participnt_data_src_code) REFERENCES farm_participnt_data_src_codes(participnt_data_src_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_farm_sgc_fk FOREIGN KEY (structure_group_code) REFERENCES farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
