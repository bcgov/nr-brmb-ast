CREATE TABLE farms.farm_rsn_rev_nursery_results (
	rsn_rev_nursery_result_id bigint NOT NULL,
	nursery_pass_ind varchar(1) NOT NULL,
	reported_revenue decimal(13,2),
	expected_revenue decimal(25,2),
	revenue_variance decimal(16,3),
	revenue_variance_limit decimal(16,3) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_nursery_results IS E'RSN REV NURSERY RESULT contains the results of the Revenue Risk - Nursery Subtest of the scenario.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.expected_revenue IS E'EXPECTED REVENUE is the total revenue expected based on REPORTED INVENTORY items with COMMODITY TYPE CODE: NURSERY.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.nursery_pass_ind IS E'NURSERY PASS IND denotes whether Revenue Risk - Nursery subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.reported_revenue IS E'REPORTED REVENUE is the total revenue reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: NURSERY.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revenue_variance IS E'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revenue_variance_limit IS E'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.rsn_rev_nursery_result_id IS E'RSN REV NURSERY RESULT ID is a surrogate unique identifier for RSN REV NURSERY RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_rsn_rev_nursery_results ADD CONSTRAINT farm_rrnr_pk PRIMARY KEY (rsn_rev_nursery_result_id);
ALTER TABLE farms.farm_rsn_rev_nursery_results ADD CONSTRAINT farm_rrnr_uk UNIQUE (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_nursery_results ADD CONSTRAINT farm_rrnr_pass_chk CHECK (nursery_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN rsn_rev_nursery_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN nursery_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN revenue_variance_limit SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_nursery_results ALTER COLUMN when_created SET NOT NULL;
