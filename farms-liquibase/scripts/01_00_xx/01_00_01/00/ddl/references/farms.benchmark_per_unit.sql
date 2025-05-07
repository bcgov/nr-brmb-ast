ALTER TABLE benchmark_per_unit ADD CONSTRAINT fk_bpu_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE benchmark_per_unit ADD CONSTRAINT fk_bpu_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES municipality_code(municipality_code)
;

ALTER TABLE benchmark_per_unit ADD CONSTRAINT fk_bpu_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES structure_group_code(structure_group_code)
;
