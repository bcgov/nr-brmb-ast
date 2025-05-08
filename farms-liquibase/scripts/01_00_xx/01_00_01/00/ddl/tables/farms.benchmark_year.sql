CREATE TABLE benchmark_year(
    benchmark_year           numeric(4, 0)     NOT NULL,
    average_margin           numeric(13, 2)    NOT NULL,
    average_expense          numeric(13, 2),
    benchmark_per_unit_id    numeric(10, 0)    NOT NULL,
    revision_count           numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user              varchar(30)       NOT NULL,
    create_date              timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user              varchar(30),
    update_date              timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN benchmark_year.benchmark_year IS 'BENCHMARK YEAR is the year this data pertains to.  For each PROGRAM YEAR there should be six years of benchmark data for each inventory type.'
;
COMMENT ON COLUMN benchmark_year.average_margin IS 'AVERAGE MARGIN is the average income minus expenses for growing a certain crop on a model farm.'
;
COMMENT ON COLUMN benchmark_year.average_expense IS 'AVERAGE EXPENSE is the average expenses for growing a certain crop on a model farm. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN benchmark_year.benchmark_per_unit_id IS 'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN benchmark_year.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN benchmark_year.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN benchmark_year.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN benchmark_year.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN benchmark_year.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE benchmark_year IS 'BENCHMARK YEAR is the income minus expenses for growing a certain crop on a model farm for a specific year.'
;


CREATE INDEX iix_by_bpui ON benchmark_year(benchmark_per_unit_id)
 TABLESPACE pg_default
;

ALTER TABLE benchmark_year ADD 
    CONSTRAINT pk_by PRIMARY KEY (benchmark_year, benchmark_per_unit_id) USING INDEX TABLESPACE pg_default 
;
