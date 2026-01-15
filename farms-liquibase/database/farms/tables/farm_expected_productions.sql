CREATE TABLE farms.farm_expected_productions (
	expected_production_id bigint NOT NULL,
	expected_prodctn_per_prod_unit decimal(13,3) NOT NULL,
	inventory_item_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_expected_productions IS E'EXPECTED PRODUCTION is the amount of a particular crop that a farm can typically produce. The amount varies between municipalities. The EXPECTED PRODUCTION is used in a reasonability test as a basis for comparison with a producer''s reported quantity produced in REPORTED INVENTORY.';
COMMENT ON COLUMN farms.farm_expected_productions.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_expected_productions.expected_prodctn_per_prod_unit IS E'EXPECTED PRODCTN PER PROD UNIT is the amount typically produced per productive unit for this crop, in the CROP UNIT CODE specified.';
COMMENT ON COLUMN farms.farm_expected_productions.expected_production_id IS E'EXPECTED PRODUCTION ID is a surrogate unique identifier for an EXPECTED PRODUCTION.';
COMMENT ON COLUMN farms.farm_expected_productions.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_expected_productions.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_expected_productions.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_expected_productions.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_expected_productions.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_expected_productions.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_ep_farm_cuc_fk_i ON farms.farm_expected_productions (crop_unit_code);
ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_uk UNIQUE (inventory_item_code);
ALTER TABLE farms.farm_expected_productions ADD CONSTRAINT farm_ep_pk PRIMARY KEY (expected_production_id);
ALTER TABLE farms.farm_expected_productions ALTER COLUMN expected_production_id SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN expected_prodctn_per_prod_unit SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_expected_productions ALTER COLUMN when_created SET NOT NULL;
