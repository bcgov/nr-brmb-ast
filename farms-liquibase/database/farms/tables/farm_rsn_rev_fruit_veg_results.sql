CREATE TABLE farms.farm_rsn_rev_fruit_veg_results (
	rsn_rev_fruit_veg_result_id bigint NOT NULL,
	reported_revenue decimal(13,2),
	quantity_produced decimal(19,3),
	expected_price decimal(13,2),
	expected_revenue decimal(25,2),
	revenue_variance decimal(19,3),
	revenue_variance_limit decimal(16,3),
	revenue_within_limit_ind varchar(1),
	fruit_veg_type_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_fruit_veg_results IS E'RSN REV FRUIT VEG RESULT contains the calculated results for the Reasonability Test: Fruits and Vegetables Revenue Test run against the scenario, by FRUIT VEG TYPE CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.expected_price IS E'EXPECTED PRICE is the benchmark price per CROP UNIT CODE that the farm could be expected to earn under normal circumstances.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.expected_revenue IS E'EXPECTED REVENUE is the PRODUCTION POUNDS multiplied by the EXPECTED PRICE PER POUND.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.fruit_veg_type_code IS E'FRUIT VEG TYPE CODE is a unique code for the object FRUIT VEG TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.quantity_produced IS E'QUANTITY PRODUCED is the quantity produced from REPORTED INVENTORY.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.reported_revenue IS E'REPORTED REVENUE is the total income for this FRUIT VEG TYPE CODE. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.revenue_variance IS E'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.revenue_variance_limit IS E'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.revenue_within_limit_ind IS E'REVENUE WITHIN LIMIT IND indicates whether REPORTED REVENUE is within the configured limit of the EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.rsn_rev_fruit_veg_result_id IS E'RSN REV FRUIT VEG RESULT ID is a surrogate unique identifier for RSN REV FRUIT VEG RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rrfvr_farm_cuc_fk_i ON farms.farm_rsn_rev_fruit_veg_results (crop_unit_code);
CREATE INDEX farm_rrfvr_farm_fvtc_fk_i ON farms.farm_rsn_rev_fruit_veg_results (fruit_veg_type_code);
CREATE INDEX farm_rrfvr_farm_rtr_fk_i ON farms.farm_rsn_rev_fruit_veg_results (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT farm_rrfvr_uk UNIQUE (reasonability_test_result_id,fruit_veg_type_code);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT farm_rrfvr_pk PRIMARY KEY (rsn_rev_fruit_veg_result_id);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ADD CONSTRAINT farm_rrfvr_rwl_chk CHECK (revenue_within_limit_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN rsn_rev_fruit_veg_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN fruit_veg_type_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_results ALTER COLUMN when_created SET NOT NULL;
