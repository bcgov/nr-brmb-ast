CREATE TABLE farms.farm_rsn_rev_poultry_brl_rslts (
	rsn_rev_poultry_brl_rslt_id bigint NOT NULL,
	has_poultry_broilers_ind varchar(1) NOT NULL,
	poultry_broiler_pass_ind varchar(1) NOT NULL,
	has_chickens_ind varchar(1) NOT NULL,
	chicken_pass_ind varchar(1) NOT NULL,
	chicken_kg_produced decimal(13,3),
	chicken_avg_weight_kg decimal(16,3) NOT NULL,
	chicken_expected_birds_sold decimal(14,3),
	chicken_price_per_bird decimal(13,2),
	chicken_expected_revenue decimal(25,2),
	chicken_reported_revenue decimal(13,2),
	chicken_revenue_variance decimal(16,3),
	chicken_revenue_variance_limit decimal(16,3) NOT NULL,
	has_turkeys_ind varchar(1) NOT NULL,
	turkey_pass_ind varchar(1) NOT NULL,
	turkey_kg_produced decimal(13,3),
	turkey_avg_weight_kg decimal(16,3) NOT NULL,
	turkey_expected_birds_sold decimal(14,3),
	turkey_price_per_bird decimal(13,2),
	turkey_expected_revenue decimal(25,2),
	turkey_reported_revenue decimal(13,2),
	turkey_revenue_variance decimal(16,3),
	turkey_revenue_variance_limit decimal(16,3) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_poultry_brl_rslts IS E'RSN REV POULTRY BRL RSLT contains the results of the Revenue Risk - Poultry - Broilers Subtest of the scenario.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_avg_weight_kg IS E'CHICKEN AVG WEIGHT KG is the configured average weight of a broiler chicken in Kilograms.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_expected_birds_sold IS E'CHICKEN EXPECTED BIRDS SOLD is the number of chickens we expect the producer to have sold based on their productive units (kg produced) and the average weight of a chicken.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_expected_revenue IS E'CHICKEN EXPECTED REVENUE is the expected revenue from broiler chickens based on the CHICKEN EXPECTED BIRDS SOLD and CHICKEN PRICE PER BIRD.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_kg_produced IS E'CHICKEN KG PRODUCED is the Kilograms of chicken produced. This is the productive units from PRODUCTVE UNIT CAPACITY.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_pass_ind IS E'CHICKEN PASS IND denotes whether the chicken part of this subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_price_per_bird IS E'CHICKEN PRICE PER BIRD is the FMV Price End for a chicken based on the fiscal year end of the operation and the FMV price for inventory code 7681.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_reported_revenue IS E'CHICKEN REPORTED REVENUE is the revenue reported for chicken LINE ITEMs in FARM REPORTED INCOME EXPENSE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_revenue_variance IS E'CHICKEN REVENUE VARIANCE is the percent difference between CHICKEN REPORTED REVENUE and CHICKEN EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.chicken_revenue_variance_limit IS E'CHICKEN REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between CHICKEN REPORTED REVENUE and CHICKEN EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.has_chickens_ind IS E'HAS CHICKENS IND denotes whether the this operation has broiler chickens.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.has_poultry_broilers_ind IS E'HAS POULTRY BROILERS IND denotes whether this operation has poultry broilers.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.has_turkeys_ind IS E'HAS TURKEYS IND denotes whether the this operation has broiler turkeys.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.poultry_broiler_pass_ind IS E'POULTRY BROILER PASS IND denotes whether Revenue Risk - Poultry - Broilers subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.rsn_rev_poultry_brl_rslt_id IS E'RSN REV POULTRY BRL RSLT ID is a surrogate unique identifier for RSN REV POULTRY BRL RSLT.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_avg_weight_kg IS E'TURKEY AVG WEIGHT KG is the configured average weight of a broiler turkey in Kilograms.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_expected_birds_sold IS E'TURKEY EXPECTED BIRDS SOLD is the number of turkeys we expect the producer to have sold based on their productive units (kg produced) and the average weight of a turkey.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_expected_revenue IS E'TURKEY EXPECTED REVENUE is the expected revenue from broiler turkeys based on the TURKEY EXPECTED BIRDS SOLD and TURKEY PRICE PER BIRD.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_kg_produced IS E'TURKEY KG PRODUCED is the Kilograms of turkey produced. This is the productive units from PRODUCTVE UNIT CAPACITY.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_pass_ind IS E'TURKEY PASS IND denotes whether the turkey part of this subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_price_per_bird IS E'TURKEY PRICE PER BIRD is the FMV Price End for a turkey based on the fiscal year end of the operation and the FMV price for inventory code 7681.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_reported_revenue IS E'TURKEY REPORTED REVENUE is the revenue reported for turkey LINE ITEMs in FARM REPORTED INCOME EXPENSE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_revenue_variance IS E'TURKEY REVENUE VARIANCE is the percent difference between TURKEY REPORTED REVENUE and TURKEY EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.turkey_revenue_variance_limit IS E'TURKEY REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between TURKEY REPORTED REVENUE and TURKEY EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_brl_rslts.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_pk PRIMARY KEY (rsn_rev_poultry_brl_rslt_id);
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_uk UNIQUE (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_cp_chk CHECK (chicken_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_hc_chk CHECK (has_chickens_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_hpb_chk CHECK (has_poultry_broilers_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_ht_chk CHECK (has_turkeys_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_pass_chk CHECK (poultry_broiler_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ADD CONSTRAINT farm_rrpbr_tp_chk CHECK (turkey_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN rsn_rev_poultry_brl_rslt_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN has_poultry_broilers_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN poultry_broiler_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN has_chickens_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN chicken_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN chicken_avg_weight_kg SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN chicken_revenue_variance_limit SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN has_turkeys_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN turkey_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN turkey_avg_weight_kg SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN turkey_revenue_variance_limit SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_brl_rslts ALTER COLUMN when_created SET NOT NULL;
