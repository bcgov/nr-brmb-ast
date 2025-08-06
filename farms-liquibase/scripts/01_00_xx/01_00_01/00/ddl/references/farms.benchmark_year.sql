ALTER TABLE farms.farm_benchmark_years ADD CONSTRAINT fk_by_bpu 
    FOREIGN KEY (benchmark_per_unit_id)
    REFERENCES farms.farm_benchmark_per_units(benchmark_per_unit_id)
;
