ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT fk_bpu_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT fk_bpu_mc 
    FOREIGN KEY (municipality_code)
    REFERENCES farms.farm_municipality_codes(municipality_code)
;

ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT fk_bpu_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.farm_structure_group_codes(structure_group_code)
;
