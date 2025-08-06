ALTER TABLE farms.farm_tip_benchmark_expenses ADD CONSTRAINT fk_tbe_tby 
    FOREIGN KEY (tip_benchmark_year_id)
    REFERENCES farms.farm_tip_benchmark_years(tip_benchmark_year_id)
;
