CREATE TABLE farms.farm_fruit_veg_type_details (
	fruit_veg_type_detail_id bigint NOT NULL,
	revenue_variance_limit decimal(16,3) NOT NULL,
	fruit_veg_type_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_fruit_veg_type_details IS E'FRUIT VEG DETAIL defines details of a FRUIT VEG TYPE CODE for use with the Reasonability Test: Fruits and Vegetables Revenue Test.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.fruit_veg_type_code IS E'FRUIT VEG TYPE CODE is a unique code for the object FRUIT VEG TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.fruit_veg_type_detail_id IS E'FRUIT VEG TYPE DETAIL ID is a surrogate unique identifier for a FRUIT VEG DETAIL.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.revenue_variance_limit IS E'REVENUE VARIANCE LIMIT is the variance limit on the revenue of a FRUIT VEG TYPE CODE when comparing it to the EXPECTED PRICE PER POUND. For use with the Reasonability Test: Fruits and Vegetables Revenue Test. If a scenario has a variance greater than the defined limit for a FRUIT VEG TYPE CODE, the test will fail.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_fruit_veg_type_details.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_fruit_veg_type_details ADD CONSTRAINT farm_fvtd_pk PRIMARY KEY (fruit_veg_type_detail_id);
ALTER TABLE farms.farm_fruit_veg_type_details ADD CONSTRAINT farm_fvtd_uk UNIQUE (fruit_veg_type_code);
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN fruit_veg_type_detail_id SET NOT NULL;
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN revenue_variance_limit SET NOT NULL;
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN fruit_veg_type_code SET NOT NULL;
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_fruit_veg_type_details ALTER COLUMN when_created SET NOT NULL;
