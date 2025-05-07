CREATE TABLE benchmark_per_unit(
    benchmark_per_unit_id    numeric(10, 0)    NOT NULL,
    program_year             numeric(4, 0)     NOT NULL,
    unit_comment             varchar(2000),
    expiry_date              date,
    municipality_code        varchar(10)       NOT NULL,
    inventory_item_code      varchar(10),
    structure_group_code     varchar(10),
    revision_count           numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user              varchar(30)       NOT NULL,
    create_date              timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user              varchar(30),
    update_date              timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN benchmark_per_unit.benchmark_per_unit_id IS 'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN benchmark_per_unit.program_year IS 'PROGRAM YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN benchmark_per_unit.unit_comment IS 'UNIT COMMENT are notes that may be optionally provided by the user about the units used to measure the benchmark data.'
;
COMMENT ON COLUMN benchmark_per_unit.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN benchmark_per_unit.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.'
;
COMMENT ON COLUMN benchmark_per_unit.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN benchmark_per_unit.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN benchmark_per_unit.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN benchmark_per_unit.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN benchmark_per_unit.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN benchmark_per_unit.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN benchmark_per_unit.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE benchmark_per_unit IS 'BENCHMARK PER UNIT is the cost for growing a certain amount of a certain crop on a model farm.  The price can have regional differences, for example growing 1 acre of field tomatoes on a farm in the far north might have extra heating and transportation costs compared to a lower-mainland tomato farm.'
;


CREATE INDEX ix_bpu_iic ON benchmark_per_unit(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_bpu_mc ON benchmark_per_unit(municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_bpu_sgc ON benchmark_per_unit(structure_group_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_bpu_py_iic_mc ON benchmark_per_unit(program_year, inventory_item_code, municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_bpu_py ON benchmark_per_unit(program_year)
 TABLESPACE pg_default
;

ALTER TABLE benchmark_per_unit ADD 
    CONSTRAINT pk_bpu PRIMARY KEY (benchmark_per_unit_id) USING INDEX TABLESPACE pg_default 
;
