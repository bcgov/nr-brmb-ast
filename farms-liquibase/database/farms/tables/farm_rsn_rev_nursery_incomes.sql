CREATE TABLE farms.farm_rsn_rev_nursery_incomes (
	rsn_rev_nursery_income_id bigint NOT NULL,
	line_item smallint NOT NULL,
	reported_revenue decimal(13,2) NOT NULL,
	rsn_rev_nursery_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_nursery_incomes IS E'RSN REV NURSERY INCOME contains the calculated amounts for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by LINE ITEM.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.line_item IS E'LINE ITEM is income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.reported_revenue IS E'REPORTED REVENUE is the total income for this LINE ITEM. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.rsn_rev_nursery_income_id IS E'RSN REV NURSERY INCOME ID is a surrogate unique identifier for RSN REV NURSERY INCOME.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.rsn_rev_nursery_result_id IS E'RSN REV NURSERY RESULT ID is a surrogate unique identifier for RSN REV NURSERY RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ADD CONSTRAINT farm_rrnir_uk UNIQUE (rsn_rev_nursery_result_id,line_item);
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ADD CONSTRAINT farm_rrnir_pk PRIMARY KEY (rsn_rev_nursery_income_id);
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN rsn_rev_nursery_income_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN reported_revenue SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN rsn_rev_nursery_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_incomes ALTER COLUMN when_created SET NOT NULL;
