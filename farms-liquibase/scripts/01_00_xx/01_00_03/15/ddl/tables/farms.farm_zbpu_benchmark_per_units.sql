-- create new table
CREATE TABLE farms.farm_zbpu_benchmark_per_units_new (
    line_number bigint NOT NULL,
    program_year smallint NOT NULL,
    municipality_code varchar(10) NOT NULL,
    inventory_item_code varchar(240) NOT NULL,
    unit_comment varchar(2000),
    year_minus_6_margin decimal(13,2) NOT NULL,
    year_minus_5_margin decimal(13,2) NOT NULL,
    year_minus_4_margin decimal(13,2) NOT NULL,
    year_minus_3_margin decimal(13,2) NOT NULL,
    year_minus_2_margin decimal(13,2) NOT NULL,
    year_minus_1_margin decimal(13,2) NOT NULL,
    year_minus_6_expense decimal(13,2),
    year_minus_5_expense decimal(13,2),
    year_minus_4_expense decimal(13,2),
    year_minus_3_expense decimal(13,2),
    year_minus_2_expense decimal(13,2),
    year_minus_1_expense decimal(13,2),
    file_location varchar(256),
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.line_number IS E'LINE NUMBER is the number of a line in a CSV file. Everytime a BPU CSV file is loaded, all the BPU staging data will be deleted. Also used for generating errors to the user about data problems in the input file.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the BENCHMARK PER UNIT.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.program_year IS E'PROGRAM YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.unit_comment IS E'UNIT COMMENT are notes that may be optionally provided by the user about the units used to measure the benchmark data.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_1_expense IS E'YEAR MINUS 1 EXPENSE is the average expense for the PROGRAM YEAR minus one year. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_1_margin IS E'YEAR MINUS 1 MARGIN is the average margin for the PROGRAM YEAR minus 1 years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_2_expense IS E'YEAR MINUS 2 EXPENSE is the average expense for the PROGRAM YEAR minus two years. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_2_margin IS E'YEAR MINUS 2 MARGIN is the average margin for the PROGRAM YEAR minus 2 years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_3_expense IS E'YEAR MINUS 3 EXPENSE is the average expense for the PROGRAM YEAR minus three years. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_3_margin IS E'YEAR MINUS 3 MARGIN is the average margin for the PROGRAM YEAR minus 3 years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_4_expense IS E'YEAR MINUS 4 EXPENSE is the average expense for the PROGRAM YEAR minus four years. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_4_margin IS E'YEAR MINUS 4 MARGIN is the average margin for the PROGRAM YEAR minus 4 years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_5_expense IS E'YEAR MINUS 5 EXPENSE is the average expense for the PROGRAM YEAR minus five years. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_5_margin IS E'YEAR MINUS 5 MARGIN is the average margin for the PROGRAM YEAR minus 5 years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_6_expense IS E'YEAR MINUS 6 EXPENSE is the average expense for the PROGRAM YEAR minus six years. Used only for 2013 scenarios and forward. Required for 2013 forward.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.year_minus_6_margin IS E'YEAR MINUS 6 MARGIN is the average margin for the PROGRAM YEAR minus six years.';
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units_new.file_location IS E'FILE LOCATION is the URL of the file in SharePoint Online.';

-- populate new table
INSERT INTO farms.farm_zbpu_benchmark_per_units_new (
    line_number,
    program_year,
    municipality_code,
    inventory_item_code,
    unit_comment,
    year_minus_6_margin,
    year_minus_5_margin,
    year_minus_4_margin,
    year_minus_3_margin,
    year_minus_2_margin,
    year_minus_1_margin,
    year_minus_6_expense,
    year_minus_5_expense,
    year_minus_4_expense,
    year_minus_3_expense,
    year_minus_2_expense,
    year_minus_1_expense,
    file_location,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
)
SELECT line_number,
       program_year,
       municipality_code,
       inventory_item_code,
       unit_comment,
       year_minus_6_margin,
       year_minus_5_margin,
       year_minus_4_margin,
       year_minus_3_margin,
       year_minus_2_margin,
       year_minus_1_margin,
       year_minus_6_expense,
       year_minus_5_expense,
       year_minus_4_expense,
       year_minus_3_expense,
       year_minus_2_expense,
       year_minus_1_expense,
       null,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated
FROM farms.farm_zbpu_benchmark_per_units;

-- drop indexes

-- drop constraints
ALTER TABLE farms.farm_zbpu_benchmark_per_units DROP CONSTRAINT farm_zbpu_pk;

-- drop old table
DROP TABLE farms.farm_zbpu_benchmark_per_units;

-- rename new table
ALTER TABLE farms.farm_zbpu_benchmark_per_units_new RENAME TO farm_zbpu_benchmark_per_units;

-- add indexes

-- add constraints
ALTER TABLE farms.farm_zbpu_benchmark_per_units ADD CONSTRAINT farm_zbpu_pk PRIMARY KEY (line_number);
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN line_number SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN municipality_code SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_6_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_5_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_4_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_3_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_2_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN year_minus_1_margin SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_zbpu_benchmark_per_units ALTER COLUMN when_created SET NOT NULL;

-- grant permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON farms.farm_zbpu_benchmark_per_units TO "app_farms_rest_proxy";
