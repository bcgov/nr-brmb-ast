CREATE TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts (
	rsn_bnft_rsk_prd_un_rslts_id bigint NOT NULL,
	reported_productive_capacity decimal(14,3) NOT NULL,
	consumed_productive_capacity decimal(14,3),
	net_productive_capacity decimal(14,3) NOT NULL,
	bnft_rsk_bpu_calculated decimal(13,2) NOT NULL,
	bnft_rsk_estimated_income decimal(13,2) NOT NULL,
	inventory_item_code varchar(10),
	structure_group_code varchar(10),
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts IS E'RSN BNFT RSK PRD UN RSLTS contains the calculated amounts for productive units for the Benefit Risk Assessment reasonability test run against the scenario.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_bpu_calculated IS E'BNFT RSK BPU CALCULATED is the base price per unit. This is calculated using the BPU values for the three years used in the benefit calculation and taking lead/lag into account.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_estimated_income IS E'BNFT RSK ESTIMATED INCOME is the amount of income estimated for this productive unit code. It is the product of PRODUCTIVE CAPACITY AMOUNT and BPU CALCULATED.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.consumed_productive_capacity IS E'CONSUMED PRODUCTIVE CAPACITY is the quantity consumed by (fed out to) cattle.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.net_productive_capacity IS E'NET PRODUCTIVE CAPACITY is the quantity entered in section 9 for this inventory/bpu code minus the amount fed to cattle (forage only).';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.reported_productive_capacity IS E'REPORTED PRODUCTIVE CAPACITY is the quantity entered in section 9 for this inventory/bpu code.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.rsn_bnft_rsk_prd_un_rslts_id IS E'RSN BNFT RSK PRD UN RSLTS ID is a surrogate unique identifier for RSN BNFT RSK PRD UN RSLTS.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.structure_group_code IS E'STRUCTURE GROUP CODE identifies the type of structure group.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rbrpu_farm_ic_fk_i ON farms.farm_rsn_bnft_rsk_prd_un_rslts (inventory_item_code);
CREATE INDEX farm_rbrpu_farm_sgc_fk_i ON farms.farm_rsn_bnft_rsk_prd_un_rslts (structure_group_code);
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT farm_rbrpu_uk UNIQUE (reasonability_test_result_id,inventory_item_code,structure_group_code);
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD CONSTRAINT farm_rbrpu_pk PRIMARY KEY (rsn_bnft_rsk_prd_un_rslts_id);
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN rsn_bnft_rsk_prd_un_rslts_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN reported_productive_capacity SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN net_productive_capacity SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN bnft_rsk_bpu_calculated SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN bnft_rsk_estimated_income SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ALTER COLUMN when_created SET NOT NULL;
