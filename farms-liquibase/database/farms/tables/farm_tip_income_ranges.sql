CREATE TABLE farms.farm_tip_income_ranges (
	tip_income_range_id bigint NOT NULL,
	range_low decimal(13,2) NOT NULL,
	range_high decimal(13,2) NOT NULL,
	minimum_group_count smallint NOT NULL,
	tip_farm_type_3_lookup_id bigint,
	tip_farm_type_2_lookup_id bigint,
	tip_farm_type_1_lookup_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_income_ranges IS E'TIP INCOME RANGE contains the low and high ranges of a grouping for a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.minimum_group_count IS E'MINIMUM GROUP COUNT is the minimum number of farms required before any farms can be assigned to a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.range_high IS E'RANGE HIGH is the minimum range for a grouping for a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.range_low IS E'RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_1_lookup_id IS E'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_2_lookup_id IS E'TIP FARM TYPE 2 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 2 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_3_lookup_id IS E'TIP FARM TYPE 3 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 3 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_income_range_id IS E'TIP INCOME RANGE ID is a surrogate unique identifier for a TIP INCOME RANGE.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_income_ranges.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_low_uk UNIQUE (tip_farm_type_3_lookup_id,tip_farm_type_2_lookup_id,tip_farm_type_1_lookup_id,range_low);
ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_high_uk UNIQUE (tip_farm_type_3_lookup_id,tip_farm_type_2_lookup_id,tip_farm_type_1_lookup_id,range_high);
ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_pk PRIMARY KEY (tip_income_range_id);
ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_high_low_chk CHECK (range_low < range_high);
ALTER TABLE farms.farm_tip_income_ranges ADD CONSTRAINT farm_tir_type_chk CHECK (((CASE WHEN((tip_farm_type_3_lookup_id IS NOT NULL AND tip_farm_type_3_lookup_id::text <> '') AND tip_farm_type_3_lookup_id::text <> '') THEN 1 ELSE 0 END) + (CASE WHEN((tip_farm_type_2_lookup_id IS NOT NULL AND tip_farm_type_2_lookup_id::text <> '') AND tip_farm_type_2_lookup_id::text <> '') THEN 1 ELSE 0 END) + (CASE WHEN((tip_farm_type_1_lookup_id IS NOT NULL AND tip_farm_type_1_lookup_id::text <> '') AND tip_farm_type_1_lookup_id::text <> '') THEN 1 ELSE 0 END)) <= 1);
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN tip_income_range_id SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN range_low SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN range_high SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN minimum_group_count SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_income_ranges ALTER COLUMN when_created SET NOT NULL;
