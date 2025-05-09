ALTER TABLE structure_group_attribute ADD CONSTRAINT fk_sga_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES structure_group_code(structure_group_code)
;

ALTER TABLE structure_group_attribute ADD CONSTRAINT fk_sga_sgc1 
    FOREIGN KEY (rollup_structure_group_code)
    REFERENCES structure_group_code(structure_group_code)
;
