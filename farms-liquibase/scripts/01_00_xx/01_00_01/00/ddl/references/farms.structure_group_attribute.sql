ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT fk_sga_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;

ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT fk_sga_sgc1 
    FOREIGN KEY (rollup_structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;
