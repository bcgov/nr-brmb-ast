ALTER TABLE farms.benchmark_per_unit ADD CONSTRAINT fk_bpu_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.benchmark_per_unit ADD CONSTRAINT fk_bpu_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.municipality_code(municipality_code)
;

ALTER TABLE farms.benchmark_per_unit ADD CONSTRAINT fk_bpu_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.structure_group_code(structure_group_code)
;
