CREATE TABLE farms.farm_zbpu_benchmark_per_units(
    line_number             numeric(10, 0)    NOT NULL,
    program_year            numeric(4, 0)     NOT NULL,
    municipality_code       varchar(10)       NOT NULL,
    inventory_item_code     varchar(240)      NOT NULL,
    unit_comment            varchar(2000),
    year_minus_6_margin     numeric(13, 2)    NOT NULL,
    year_minus_5_margin     numeric(13, 2)    NOT NULL,
    year_minus_4_margin     numeric(13, 2)    NOT NULL,
    year_minus_3_margin     numeric(13, 2)    NOT NULL,
    year_minus_2_margin     numeric(13, 2)    NOT NULL,
    year_minus_1_margin     numeric(13, 2)    NOT NULL,
    year_minus_6_expense    numeric(13, 2),
    year_minus_5_expense    numeric(13, 2),
    year_minus_4_expense    numeric(13, 2),
    year_minus_3_expense    numeric(13, 2),
    year_minus_2_expense    numeric(13, 2),
    year_minus_1_expense    numeric(13, 2),
    revision_count          numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created             varchar(30)       NOT NULL,
    when_created             timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated             varchar(30),
    when_updated             timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.line_number IS 'LINE NUMBER is the number of a line in a CSV file. Everytime a BPU CSV file is loaded, all the BPU staging data will be deleted. Also used for generating errors to the user about data problems in the input file.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.program_year IS 'PROGRAM YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the BENCHMARK PER UNIT.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.unit_comment IS 'UNIT COMMENT are notes that may be optionally provided by the user about the units used to measure the benchmark data.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_6_margin IS 'YEAR MINUS 6 MARGIN is the average margin for the PROGRAM YEAR minus six years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_5_margin IS 'YEAR MINUS 5 MARGIN is the average margin for the PROGRAM YEAR minus 5 years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_4_margin IS 'YEAR MINUS 4 MARGIN is the average margin for the PROGRAM YEAR minus 4 years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_3_margin IS 'YEAR MINUS 3 MARGIN is the average margin for the PROGRAM YEAR minus 3 years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_2_margin IS 'YEAR MINUS 2 MARGIN is the average margin for the PROGRAM YEAR minus 2 years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_1_margin IS 'YEAR MINUS 1 MARGIN is the average margin for the PROGRAM YEAR minus 1 years.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_6_expense IS 'YEAR MINUS 6 EXPENSE is the average expense for the PROGRAM YEAR minus six years. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_5_expense IS 'YEAR MINUS 5 EXPENSE is the average expense for the PROGRAM YEAR minus five years. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_4_expense IS 'YEAR MINUS 4 EXPENSE is the average expense for the PROGRAM YEAR minus four years. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_3_expense IS 'YEAR MINUS 3 EXPENSE is the average expense for the PROGRAM YEAR minus three years. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_2_expense IS 'YEAR MINUS 2 EXPENSE is the average expense for the PROGRAM YEAR minus two years. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.year_minus_1_expense IS 'YEAR MINUS 1 EXPENSE is the average expense for the PROGRAM YEAR minus one year. Used only for 2013 scenarios and forward. Required for 2013 forward.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zbpu_benchmark_per_units.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;



ALTER TABLE farms.farm_zbpu_benchmark_per_units ADD 
    CONSTRAINT pk_zbpu PRIMARY KEY (line_number) USING INDEX TABLESPACE pg_default 
;
