ALTER TABLE benchmark_year ADD CONSTRAINT fk_by_bpu 
    FOREIGN KEY (benchmark_per_unit_id)
    REFERENCES benchmark_per_unit(benchmark_per_unit_id)
;
