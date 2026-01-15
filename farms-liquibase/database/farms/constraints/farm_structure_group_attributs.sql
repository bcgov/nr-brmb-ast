ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_farm_iic_fk FOREIGN KEY (structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_farm_riic_fk FOREIGN KEY (rollup_structure_group_code) REFERENCES farms.farm_structure_group_codes(structure_group_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
