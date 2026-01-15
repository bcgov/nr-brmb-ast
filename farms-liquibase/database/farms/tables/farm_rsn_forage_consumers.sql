CREATE TABLE farms.farm_rsn_forage_consumers (
	rsn_forage_consumer_id bigint NOT NULL,
	productive_capacity_amount decimal(13,3) NOT NULL,
	quantity_consumed_per_unit decimal(13,3) NOT NULL,
	quantity_consumed decimal(19,3) NOT NULL,
	structure_group_code varchar(10) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_forage_consumers IS E'RSN FORAGE CONSUMER contains the calculated amounts of forage inventory consumed by (fed out to) cattle for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by STRUCTURE GROUP CODE (the type of cattle).';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.productive_capacity_amount IS E'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.quantity_consumed IS E'QUANTITY CONSUMED is the amount of forage inventory consumed by (fed out to) this type of cattle.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.quantity_consumed_per_unit IS E'QUANTITY CONSUMED PER UNIT is the amount of forage inventory consumed by (fed out to) this type of cattle per productive unit (PRODUCTIVE CAPACITY AMOUNT).';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.rsn_forage_consumer_id IS E'RSN FORAGE CONSUMER ID is a surrogate unique identifier for RSN REV FORAGE CONSUMER.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.structure_group_code IS E'STRUCTURE GROUP CODE identifies the type of structure group.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rfc_farm_sgc_fk_i ON farms.farm_rsn_forage_consumers (structure_group_code);
ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT farm_rfc_uk UNIQUE (reasonability_test_result_id,structure_group_code);
ALTER TABLE farms.farm_rsn_forage_consumers ADD CONSTRAINT farm_rfc_pk PRIMARY KEY (rsn_forage_consumer_id);
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN rsn_forage_consumer_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN productive_capacity_amount SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN quantity_consumed_per_unit SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN quantity_consumed SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN structure_group_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_forage_consumers ALTER COLUMN when_created SET NOT NULL;
