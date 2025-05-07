ALTER TABLE tip_benchmark_expense ADD CONSTRAINT fk_tbe_tby 
    FOREIGN KEY (tip_benchmark_year_id)
    REFERENCES tip_benchmark_year(tip_benchmark_year_id)
;
