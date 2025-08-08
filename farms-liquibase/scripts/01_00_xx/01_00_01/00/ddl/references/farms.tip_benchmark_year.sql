ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT fk_tby_tby 
    FOREIGN KEY (parent_tip_benchmark_year_id)
    REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id)
;
