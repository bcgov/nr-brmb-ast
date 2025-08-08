CREATE TABLE farms.farm_benchmark_years(
    benchmark_year           numeric(4, 0)     NOT NULL,
    average_margin           numeric(13, 2)    NOT NULL,
    average_expense          numeric(13, 2),
    benchmark_per_unit_id    numeric(10, 0)    NOT NULL,
    revision_count           numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created              varchar(30)       NOT NULL,
    when_created              timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated              varchar(30),
    when_updated              timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_benchmark_years.benchmark_year IS 'BENCHMARK YEAR is the year this data pertains to.  For each PROGRAM YEAR there should be six years of benchmark data for each inventory type.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.average_margin IS 'AVERAGE MARGIN is the average income minus expenses for growing a certain crop on a model farm.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.average_expense IS 'AVERAGE EXPENSE is the average expenses for growing a certain crop on a model farm. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.benchmark_per_unit_id IS 'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benchmark_years.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_benchmark_years IS 'BENCHMARK YEAR is the income minus expenses for growing a certain crop on a model farm for a specific year.'
;


CREATE INDEX iix_by_bpui ON farms.farm_benchmark_years(benchmark_per_unit_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_benchmark_years ADD 
    CONSTRAINT pk_by PRIMARY KEY (benchmark_year, benchmark_per_unit_id) USING INDEX TABLESPACE pg_default 
;
