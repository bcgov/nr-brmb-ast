CREATE TABLE farms.farm_tip_farm_type_2_lookups (
	tip_farm_type_2_lookup_id bigint NOT NULL,
	farm_type_2_name varchar(256) NOT NULL,
	tip_farm_type_3_lookup_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_farm_type_2_lookups IS E'TIP FARM TYPE 2 LOOKUP indicates how a farm gets the majority of its income. There are three levels of farm type. This is the second level. The top level is TIP FARM TYPE 3 LOOKUP and the lowest level is TIP FARM TYPE 1 LOOKUP. A farm''s type is determined by summing up its REPORTED INCOME EXPENSE items and mapping the income LINE ITEM number to a TIP FARM TYPE 1 LOOKUP. Examples of names are are Dairy, "Poultry, Chicken",  "Swine".';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.farm_type_2_name IS E'FARM TYPE 2 NAME is a textual description of the second level of TIP farm types.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.tip_farm_type_2_lookup_id IS E'TIP FARM TYPE 2 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 2 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.tip_farm_type_3_lookup_id IS E'TIP FARM TYPE 3 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 3 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_farm_type_2_lookups.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_tft2l_farm_tft3l_fk_i ON farms.farm_tip_farm_type_2_lookups (tip_farm_type_3_lookup_id);
ALTER TABLE farms.farm_tip_farm_type_2_lookups ADD CONSTRAINT farm_tft2l_uk UNIQUE (farm_type_2_name);
ALTER TABLE farms.farm_tip_farm_type_2_lookups ADD CONSTRAINT farm_tft2l_pk PRIMARY KEY (tip_farm_type_2_lookup_id);
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN tip_farm_type_2_lookup_id SET NOT NULL;
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN farm_type_2_name SET NOT NULL;
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN tip_farm_type_3_lookup_id SET NOT NULL;
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_farm_type_2_lookups ALTER COLUMN when_created SET NOT NULL;
