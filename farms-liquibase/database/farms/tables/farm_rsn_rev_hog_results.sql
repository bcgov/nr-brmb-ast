CREATE TABLE farms.farm_rsn_rev_hog_results (
	rsn_rev_hog_result_id bigint NOT NULL,
	has_hogs_ind varchar(1) NOT NULL,
	hogs_pass_ind varchar(1) NOT NULL,
	farrow_to_finish_operation_ind varchar(1) NOT NULL,
	feeder_operation_ind varchar(1) NOT NULL,
	reported_expenses decimal(13,2) NOT NULL,
	total_quantity_start decimal(14,3) NOT NULL,
	total_quantity_end decimal(14,3) NOT NULL,
	farrow_sows_breeding decimal(13,3) NOT NULL,
	farrow_births_per_cycle decimal(4,2) NOT NULL,
	farrow_birth_cycles_per_year decimal(3,2) NOT NULL,
	farrow_total_births_per_cycle decimal(16,3) NOT NULL,
	farrow_births_all_cycles decimal(16,3) NOT NULL,
	farrow_death_rate decimal(3,3) NOT NULL,
	farrow_deaths decimal(14,3) NOT NULL,
	farrow_boar_purchase_count decimal(13,2) NOT NULL,
	farrow_boar_purchase_price decimal(13,2),
	farrow_boar_purchase_expense decimal(13,2) NOT NULL,
	farrow_sow_purchase_expense decimal(13,2) NOT NULL,
	farrow_sow_purchase_count decimal(13,2) NOT NULL,
	farrow_sow_purchase_price decimal(13,2),
	feeder_productive_units decimal(13,3) NOT NULL,
	feeder_weanling_purchase_expns decimal(13,2) NOT NULL,
	feeder_weanling_purchase_price decimal(13,2),
	feeder_weanling_purchase_count decimal(13,2) NOT NULL,
	total_purchase_count decimal(13,2) NOT NULL,
	expected_sold decimal(17,3) NOT NULL,
	heaviest_hog_price decimal(13,2),
	reported_revenue decimal(13,2) NOT NULL,
	expected_revenue decimal(25,2),
	revenue_variance decimal(16,3),
	revenue_variance_limit decimal(16,3),
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_hog_results IS E'RSN REV HOG RESULT contains the results of the Revenue Risk - Hogs Subtest of the scenario.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.expected_revenue IS E'EXPECTED REVENUE is the total revenue expected based on REPORTED INVENTORY items with COMMODITY TYPE CODE: NURSERY.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.expected_sold IS E'EXPECTED SOLD is the expected number of hogs sold.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_birth_cycles_per_year IS E'FARROW BIRTH CYCLES PER YEAR is the configured average number of birth cycles per sow per year.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_births_all_cycles IS E'FARROW BIRTHS ALL CYCLES is the expected total number of births per year for all sows.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_births_per_cycle IS E'FARROW BIRTHS PER CYCLE is the configured average number of births per sow per cycle.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_boar_purchase_count IS E'FARROW BOAR PURCHASE COUNT is the expected number of boars purchased for breeding for a Farrow to Finish operation. This is estimated based on an inventory increase for boars, QUANTITY END minus QUANTITY START.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_boar_purchase_expense IS E'FARROW BOAR PURCHASE EXPENSE is the total expected expense of boar purchases. This is the FARROW BOAR PURCHASE COUNT multiplied by the FARROW BOAR PURCHASE PRICE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_boar_purchase_price IS E'FARROW BOAR PURCHASE PRICE is the reported price per boar purchased. This is the Price End from REPORTED INVENTORY for the Boar inventory code, 8752.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_death_rate IS E'FARROW DEATH RATE is the configured average percentage of deaths of pigs born.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_deaths IS E'FARROW DEATHS is the number of deaths of pigs born.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_sow_purchase_count IS E'FARROW SOW PURCHASE COUNT is the expected number of sows purchased for breeding for a Farrow to Finish operation. This is the FARROW SOW PURCHASE EXPENSE divided by the FARROW SOW PURCHASE PRICE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_sow_purchase_expense IS E'FARROW SOW PURCHASE EXPENSE is the total expected expense of sow purchases. This is estimated based on the expense amount in REPORTED INCOME EXPENSE with COMMODITY TYPE CODE: HOG, minus the FARROW BOAR PURCHASE EXPENSE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_sow_purchase_price IS E'FARROW SOW PURCHASE PRICE is the reported price per sow purchased. This is the Price End from REPORTED INVENTORY for the Sow inventory code, 8754.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_sows_breeding IS E'FARROW SOWS BREEDING is the number of female pigs breeding. This is the productive units from PRODUCTVE UNIT CAPACITY with STRUCTURE GROUP CODE 123.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_to_finish_operation_ind IS E'FARROW TO FINISH OPERATION IND denotes whether this is a Farrow to Finish Operation, meaning that they breed pigs. Determined if there is PRODUCTIVE UNIT CAPACITY for code 123.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.farrow_total_births_per_cycle IS E'FARROW TOTAL BIRTHS PER CYCLE is the total births for all sows per cycle.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.feeder_operation_ind IS E'FEEDER OPERATION IND denotes whether this is a Feeder Operation, meaning that they buy young weanling pigs and feed them until they are large enough to sell (finished weight). Determined if there is not PRODUCTIVE UNIT CAPACITY for code 123, but there is for code 124.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.feeder_productive_units IS E'FEEDER PRODUCTIVE UNITS is the number of pigs being fed until they reach finishing weight. This is the productive units from PRODUCTVE UNIT CAPACITY with STRUCTURE GROUP CODE 124.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.feeder_weanling_purchase_count IS E'FEEDER WEANLING PURCHASE COUNT is the expected number of weanlings (young pigs) purchased by a Feeder Operation. This is the FEEDER WEANLING PURCHASE EXPNS divided by the FEEDER WEANLING PURCHASE PRICE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.feeder_weanling_purchase_expns IS E'FEEDER WEANLING PURCHASE EXPNS is the total expense for a Feeder Operation of weanlings (young pigs) purchased.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.feeder_weanling_purchase_price IS E'FEEDER WEANLING PURCHASE PRICE is the reported price per weanling (young pig) by a Feeder Operation. This is the Price Start  from REPORTED INVENTORY for the Hogs; Feeder; Births inventory code, 8763.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.has_hogs_ind IS E'HAS HOGS IND denotes whether this operation has hogs.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.heaviest_hog_price IS E'HEAVIEST HOG PRICE is the End Price of the heaviest hog in the inventory of this farm.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.hogs_pass_ind IS E'HOGS PASS IND denotes whether Revenue Risk - Hogs subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.reported_expenses IS E'REPORTED EXPENSES is the total expense reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: HOG.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.reported_revenue IS E'REPORTED REVENUE is the total revenue reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: HOG.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.revenue_variance IS E'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.revenue_variance_limit IS E'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.rsn_rev_hog_result_id IS E'RSN REV HOG RESULT ID is a surrogate unique identifier for RSN REV HOG RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.total_purchase_count IS E'TOTAL PURCHASE COUNT is the total number of pigs purchased. This is the sum of FARROW BOAR PURCHASE COUNT, FARROW SOW PURCHASE COUNT, and FEEDER WEANLING PURCHASE COUNT.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.total_quantity_end IS E'TOTAL QUANTITY END is the total of QUANTITY END for REPORTED INVENTORY items with COMMODITY TYPE CODE: HOG.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.total_quantity_start IS E'TOTAL QUANTITY START is the total of QUANTITY START for REPORTED INVENTORY items with COMMODITY TYPE CODE: HOG.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_uk UNIQUE (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_pk PRIMARY KEY (rsn_rev_hog_result_id);
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_fo_chk CHECK (feeder_operation_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_ftfo_chk CHECK (farrow_to_finish_operation_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_hh_chk CHECK (has_hogs_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_hog_results ADD CONSTRAINT farm_rrhr_pass_chk CHECK (hogs_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN rsn_rev_hog_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN has_hogs_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN hogs_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_to_finish_operation_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN feeder_operation_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN reported_expenses SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN total_quantity_start SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN total_quantity_end SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_sows_breeding SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_births_per_cycle SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_birth_cycles_per_year SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_total_births_per_cycle SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_births_all_cycles SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_death_rate SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_deaths SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_boar_purchase_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_boar_purchase_expense SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_sow_purchase_expense SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN farrow_sow_purchase_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN feeder_productive_units SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN feeder_weanling_purchase_expns SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN feeder_weanling_purchase_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN total_purchase_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN expected_sold SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN reported_revenue SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_results ALTER COLUMN when_created SET NOT NULL;
