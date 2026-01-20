CREATE TABLE farms.farm_rsn_rev_fruit_veg_invntrs (
	rsn_rev_fruit_veg_invntry_id bigint NOT NULL,
	quantity_produced decimal(19,3),
	expected_price decimal(13,2),
	expected_revenue decimal(25,2),
	inventory_item_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	reasonability_test_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_fruit_veg_invntrs IS E'RSN REV FRUIT VEG INVNTRY contains the calculated inventory results for the Reasonability Test: Fruits and Vegetables Revenue Test run against the scenario, by INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.expected_price IS E'EXPECTED PRICE is the benchmark price per CROP UNIT CODE that the farm could be expected to earn under normal circumstances.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.expected_revenue IS E'EXPECTED REVENUE is the amount of revenue, inventory sold multiplied by reported price.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.quantity_produced IS E'QUANTITY PRODUCED is the quantity produced during the year from REPORTED INVENTORY in CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.rsn_rev_fruit_veg_invntry_id IS E'RSN REV FRUIT VEG INVNTRY ID is a surrogate unique identifier for RSN REV FRUIT VEG INVNTRY.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rrfvi_farm_cuc_fk_i ON farms.farm_rsn_rev_fruit_veg_invntrs (crop_unit_code);
CREATE INDEX farm_rrfvi_farm_ic_fk_i ON farms.farm_rsn_rev_fruit_veg_invntrs (inventory_item_code);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD CONSTRAINT farm_rrfvi_uk UNIQUE (reasonability_test_result_id,inventory_item_code);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD CONSTRAINT farm_rrfvi_pk PRIMARY KEY (rsn_rev_fruit_veg_invntry_id);
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN rsn_rev_fruit_veg_invntry_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ALTER COLUMN when_created SET NOT NULL;
