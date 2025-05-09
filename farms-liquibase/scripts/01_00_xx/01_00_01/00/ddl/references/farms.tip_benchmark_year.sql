ALTER TABLE farms.tip_benchmark_year ADD CONSTRAINT fk_tby_tby 
    FOREIGN KEY (parent_tip_benchmark_year_id)
    REFERENCES farms.tip_benchmark_year(tip_benchmark_year_id)
;
