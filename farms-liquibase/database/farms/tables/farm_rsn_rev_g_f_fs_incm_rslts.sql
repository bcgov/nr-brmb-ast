CREATE TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts (
	rsn_rev_g_f_fs_incm_rslt_id bigint NOT NULL,
	reported_revenue decimal(13,2),
	expected_revenue decimal(25,2),
	revenue_variance decimal(16,3),
	revenue_within_limit_ind varchar(1),
	line_item smallint NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts IS E'RSN REV G F FS INCM RSLT contains the calculated amounts for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by LINE ITEM.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.expected_revenue IS E'EXPECTED REVENUE is the amount of revenue based on inventory sold and reported prices.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.reported_revenue IS E'REPORTED REVENUE is the total income for this LINE ITEM. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_variance IS E'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_within_limit_ind IS E'REVENUE WITHIN LIMIT IND indicates whether REPORTED REVENUE is within the configured limit of the EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.rsn_rev_g_f_fs_incm_rslt_id IS E'RSN REV G F FS INCM RSLT ID is a surrogate unique identifier for RSN REV G F FS INCM RSLT.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rrgir_farm_rtr_fk_i ON farms.farm_rsn_rev_g_f_fs_incm_rslts (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ADD CONSTRAINT farm_rrgir_uk UNIQUE (reasonability_test_result_id,line_item);
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ADD CONSTRAINT farm_rrgir_pk PRIMARY KEY (rsn_rev_g_f_fs_incm_rslt_id);
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ADD CONSTRAINT farm_rrgir_rwl_chk CHECK (revenue_within_limit_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN rsn_rev_g_f_fs_incm_rslt_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ALTER COLUMN when_created SET NOT NULL;
