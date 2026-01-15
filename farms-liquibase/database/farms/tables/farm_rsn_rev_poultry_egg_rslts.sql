CREATE TABLE farms.farm_rsn_rev_poultry_egg_rslts (
	rsn_rev_poultry_egg_rslt_id bigint NOT NULL,
	has_poultry_eggs_ind varchar(1) NOT NULL,
	poultry_eggs_pass_ind varchar(1) NOT NULL,
	consumption_pass_ind varchar(1) NOT NULL,
	consumption_layers decimal(13,3),
	consumption_avg_eggs_per_layer decimal(5,1) NOT NULL,
	consumption_eggs_total decimal(18,3),
	consumption_eggs_dozen_sold decimal(17,3),
	consumption_price_per_dozen decimal(13,2),
	consumption_expected_revenue decimal(30,2),
	consumption_reported_revenue decimal(13,2),
	consumption_revenue_variance decimal(16,3),
	consumption_revenue_varinc_lmt decimal(16,3) NOT NULL,
	hatching_pass_ind varchar(1) NOT NULL,
	hatching_layers decimal(13,3),
	hatching_avg_eggs_per_layer decimal(5,1) NOT NULL,
	hatching_eggs_total decimal(18,3),
	hatching_eggs_dozen_sold decimal(17,3),
	hatching_price_per_dozen decimal(13,2),
	hatching_expected_revenue decimal(30,2),
	hatching_reported_revenue decimal(13,2),
	hatching_revenue_variance decimal(16,3),
	hatching_revenue_variance_limt decimal(16,3) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_poultry_egg_rslts IS E'RSN REV POULTRY EGG RSLT contains the results of the Revenue Risk - Poultry - Eggs Subtest of the scenario.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_avg_eggs_per_layer IS E'CONSUMPTION AVG EGGS PER LAYER is the configured average number of eggs laid for hatching per chicken (layer) per year.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_eggs_dozen_sold IS E'CONSUMPTION EGGS DOZEN SOLD is the number of dozen eggs for consumption that we expect the producer to have sold based on CONSUMPTION EGGS TOTAL.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_eggs_total IS E'CONSUMPTION EGGS TOTAL is the number of eggs for consumption that we expect the producer to have produced based on their productive units (layers) and the average eggs produced per layer.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_expected_revenue IS E'CONSUMPTION EXPECTED REVENUE is the expected revenue from eggs for consumption based on the CONSUMPTION EXPECTED DOZEN SOLD and CONSUMPTION PRICE PER DOZEN.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_layers IS E'CONSUMPTION LAYERS is the chickens (layers) laying eggs for consumption. This is the productive units from PRODUCTVE UNIT CAPACITY.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_pass_ind IS E'CONSUMPTION PASS IND denotes whether the consumption part of this subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_price_per_dozen IS E'CONSUMPTION PRICE PER DOZEN is the FMV Price End for a dozen eggs for consumption based on the fiscal year end of the operation and the FMV price for inventory code 7664.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_reported_revenue IS E'CONSUMPTION REPORTED REVENUE is the revenue reported for consumption LINE ITEMs in FARM REPORTED INCOME EXPENSE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_revenue_variance IS E'CONSUMPTION REVENUE VARIANCE is the percent difference between CONSUMPTION REPORTED REVENUE and CONSUMPTION EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.consumption_revenue_varinc_lmt IS E'CONSUMPTION REVENUE VARINC LMT is the configured limit for the percentage difference between CONSUMPTION REPORTED REVENUE and CONSUMPTION EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.has_poultry_eggs_ind IS E'HAS POULTRY EGGS IND denotes whether this operation has poultry eggs.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_avg_eggs_per_layer IS E'HATCHING AVG EGGS PER LAYER is the configured average number of eggs laid for hatching per chicken (layer) per year.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_eggs_dozen_sold IS E'HATCHING EGGS DOZEN SOLD is the number of dozen eggs for consumption that we expect the producer to have sold based on HATCHING EGGS TOTAL.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_eggs_total IS E'HATCHING EGGS TOTAL is the number of eggs for hatching that we expect the producer to have produced based on their productive units (layers) and the average eggs produced per layer.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_expected_revenue IS E'HATCHING EXPECTED REVENUE is the expected revenue from eggs for hatching based on the HATCHING EXPECTED DOZEN SOLD and HATCHING PRICE PER DOZEN.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_layers IS E'HATCHING LAYERS is the chickens (layers) laying eggs for hatching. This is the productive units from PRODUCTVE UNIT CAPACITY.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_pass_ind IS E'HATCHING PASS IND denotes whether the hatching part of this subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_price_per_dozen IS E'HATCHING PRICE PER DOZEN is the FMV Price End for a dozen eggs for hatching based on the fiscal year end of the operation and the FMV price for inventory code 7663.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_reported_revenue IS E'HATCHING REPORTED REVENUE is the revenue reported for hatching LINE ITEMs in FARM REPORTED INCOME EXPENSE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_revenue_variance IS E'HATCHING REVENUE VARIANCE is the percent difference between HATCHING REPORTED REVENUE and HATCHING EXPECTED REVENUE.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.hatching_revenue_variance_limt IS E'HATCHING REVENUE VARIANCE LIMT is the configured limit for the percentage difference between HATCHING REPORTED REVENUE and HATCHING EXPECTED REVENUE. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.poultry_eggs_pass_ind IS E'POULTRY EGGS PASS IND denotes whether Revenue Risk - Poultry - Eggs subtest passed.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.rsn_rev_poultry_egg_rslt_id IS E'RSN REV POULTRY EGG RSLT ID is a surrogate unique identifier for RSN REV POULTRY EGG RSLT.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_poultry_egg_rslts.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_uk UNIQUE (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_pk PRIMARY KEY (rsn_rev_poultry_egg_rslt_id);
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_cp_chk CHECK (consumption_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_hpe_chk CHECK (has_poultry_eggs_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_hp_chk CHECK (hatching_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ADD CONSTRAINT farm_rrpe_pass_chk CHECK (poultry_eggs_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN rsn_rev_poultry_egg_rslt_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN has_poultry_eggs_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN poultry_eggs_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN consumption_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN consumption_avg_eggs_per_layer SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN consumption_revenue_varinc_lmt SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN hatching_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN hatching_avg_eggs_per_layer SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN hatching_revenue_variance_limt SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_poultry_egg_rslts ALTER COLUMN when_created SET NOT NULL;
