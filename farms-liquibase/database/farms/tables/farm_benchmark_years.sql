CREATE TABLE farms.farm_benchmark_years (
	benchmark_year smallint NOT NULL,
	average_margin decimal(13,2) NOT NULL,
	average_expense decimal(13,2),
	benchmark_per_unit_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_benchmark_years IS E'BENCHMARK YEAR is the income minus expenses for growing a certain crop on a model farm for a specific year.';
COMMENT ON COLUMN farms.farm_benchmark_years.average_expense IS E'AVERAGE EXPENSE is the average expenses for growing a certain crop on a model farm. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_benchmark_years.average_margin IS E'AVERAGE MARGIN is the average income minus expenses for growing a certain crop on a model farm.';
COMMENT ON COLUMN farms.farm_benchmark_years.benchmark_per_unit_id IS E'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.';
COMMENT ON COLUMN farms.farm_benchmark_years.benchmark_year IS E'BENCHMARK YEAR is the year this data pertains to.  For each PROGRAM YEAR there should be six years of benchmark data for each inventory type.';
COMMENT ON COLUMN farms.farm_benchmark_years.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_benchmark_years.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_benchmark_years.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_benchmark_years.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_benchmark_years.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_bpuy_farm_bpu_fk_i ON farms.farm_benchmark_years (benchmark_per_unit_id);
ALTER TABLE farms.farm_benchmark_years ADD CONSTRAINT farm_bpuy_pk PRIMARY KEY (benchmark_year,benchmark_per_unit_id);
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN benchmark_year SET NOT NULL;
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN average_margin SET NOT NULL;
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN benchmark_per_unit_id SET NOT NULL;
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_benchmark_years ALTER COLUMN when_created SET NOT NULL;
