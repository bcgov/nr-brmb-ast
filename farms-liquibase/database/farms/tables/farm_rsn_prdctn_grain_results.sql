CREATE TABLE farms.farm_rsn_prdctn_grain_results (
	rsn_prdctn_grain_result_id bigint NOT NULL,
	productive_capacity_amount decimal(14,3),
	reported_quantity_produced decimal(19,3),
	expected_production_per_unit decimal(14,3),
	expected_quantity_produced decimal(19,3),
	quantity_produced_variance decimal(19,3),
	quantity_produced_wthn_lmt_ind varchar(1),
	inventory_item_code varchar(10) NOT NULL,
	qty_produced_crop_unit_code varchar(10) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_prdctn_grain_results IS E'RSN PRDCTN GRAIN RESULT contains the calculated amounts for the Reasonability Test: Grains Production Test run against the scenario, by GRAIN TYPE CODE.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.expected_production_per_unit IS E'EXPECTED PRODUCTION PER UNIT is the amount typically produced per productive unit for this crop, in the QTY PRODUCED CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.expected_quantity_produced IS E'EXPECTED QUANTITY PRODUCED is the amount that the farm can be expected to produce for this crop based on the number of productive units and the EXPECTED PRODUCTION PER UNIT, in the QTY PRODUCED CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.productive_capacity_amount IS E'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.qty_produced_crop_unit_code IS E'QTY PRODUCED CROP UNIT CODE indicates the crop unit for REPORTED QUANTITY PRODUCED and EXPECTED QUANTITY PRODUCED.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.quantity_produced_variance IS E'QUANTITY PRODUCED VARIANCE is the percent difference between EXPECTED QUANTITY PRODUCED and REPORTED QUANTITY PRODUCED ((RQP / EQP) - 1)';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.quantity_produced_wthn_lmt_ind IS E'QUANTITY PRODUCED WTHN LMT IND indicates if the QUANTITY PRODUCED VARIANCE was within the QUANTITY PRODUCED LIMIT.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.reported_quantity_produced IS E'REPORTED QUANTITY PRODUCED is the amount produced for this crop, in the QTY PRODUCED CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.rsn_prdctn_grain_result_id IS E'RSN PRDCTN GRAIN RESULT ID is a surrogate unique identifier for RSN PRDCTN GRAIN RESULT.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_prdctn_grain_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rpgr_farm_cuc_fk_i ON farms.farm_rsn_prdctn_grain_results (qty_produced_crop_unit_code);
CREATE INDEX farm_rpgr_farm_ic_fk_i ON farms.farm_rsn_prdctn_grain_results (inventory_item_code);
CREATE INDEX farm_rpgr_farm_rtr_fk_i ON farms.farm_rsn_prdctn_grain_results (reasonability_test_result_id);
ALTER TABLE farms.farm_rsn_prdctn_grain_results ADD CONSTRAINT farm_rpgr_pk PRIMARY KEY (rsn_prdctn_grain_result_id);
ALTER TABLE farms.farm_rsn_prdctn_grain_results ADD CONSTRAINT farm_rpgr_uk UNIQUE (reasonability_test_result_id,inventory_item_code);
ALTER TABLE farms.farm_rsn_prdctn_grain_results ADD CONSTRAINT farm_rpgr_qpwl_chk CHECK (quantity_produced_wthn_lmt_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN rsn_prdctn_grain_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN qty_produced_crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_prdctn_grain_results ALTER COLUMN when_created SET NOT NULL;
