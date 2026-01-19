CREATE TABLE farms.farm_benchmark_per_units (
	benchmark_per_unit_id bigint NOT NULL,
	program_year smallint NOT NULL,
	unit_comment varchar(2000),
	expiry_date timestamp(0),
	municipality_code varchar(10) NOT NULL,
	inventory_item_code varchar(10),
	structure_group_code varchar(10),
	url_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_benchmark_per_units IS E'BENCHMARK PER UNIT is the cost for growing a certain amount of a certain crop on a model farm.  The price can have regional differences, for example growing 1 acre of field tomatoes on a farm in the far north might have extra heating and transportation costs compared to a lower-mainland tomato farm.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.benchmark_per_unit_id IS E'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.expiry_date IS E'EXPIRY DATE identifies the date this record is no longer valid.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_benchmark_per_units.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.program_year IS E'PROGRAM YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.structure_group_code IS E'STRUCTURE GROUP CODE identifies the type of structure group.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.unit_comment IS E'UNIT COMMENT are notes that may be optionally provided by the user about the units used to measure the benchmark data.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_benchmark_per_units.url_id IS E'URL ID is a foreign key to FARM_URLS.';
CREATE INDEX farm_bpu_farm_ic_fk_i ON farms.farm_benchmark_per_units (inventory_item_code);
CREATE INDEX farm_bpu_farm_mc_fk_i ON farms.farm_benchmark_per_units (municipality_code);
CREATE INDEX farm_bpu_farm_sgc_fk_i ON farms.farm_benchmark_per_units (structure_group_code);
CREATE INDEX farm_bpu_import_i ON farms.farm_benchmark_per_units (program_year, inventory_item_code, municipality_code);
CREATE INDEX farm_bpu_year_i ON farms.farm_benchmark_per_units (program_year);
ALTER TABLE farms.farm_benchmark_per_units ADD CONSTRAINT farm_bpu_pk PRIMARY KEY (benchmark_per_unit_id);
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN benchmark_per_unit_id SET NOT NULL;
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN municipality_code SET NOT NULL;
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_benchmark_per_units ALTER COLUMN when_created SET NOT NULL;
