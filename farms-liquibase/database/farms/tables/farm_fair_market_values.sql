CREATE TABLE farms.farm_fair_market_values (
	fair_market_value_id bigint NOT NULL,
	program_year smallint NOT NULL,
	period smallint NOT NULL,
	average_price decimal(13,2) NOT NULL,
	percent_variance decimal(5,2) NOT NULL,
	expiry_date timestamp(0),
	inventory_item_code varchar(10) NOT NULL,
	municipality_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	url_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_fair_market_values IS E'FAIR MARKET VALUE is the market price for selling a ceratin amount of a crop. The price varies between municipalities and between months of the year. Within a month the price can vary by a certain percentage. The FAIR MARKET VALUE is used as a basis for comparison with a producer''s reported income and expenses.';
COMMENT ON COLUMN farms.farm_fair_market_values.average_price IS E'AVERAGE PRICE is the average market price obtained for a crop for a given month.';
COMMENT ON COLUMN farms.farm_fair_market_values.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_fair_market_values.expiry_date IS E'EXPIRY DATE identifies the date this record is no longer valid.';
COMMENT ON COLUMN farms.farm_fair_market_values.fair_market_value_id IS E'FAIR MARKET VALUE ID is a surrogate unique identifier for FAIR MARKET VALUEs.';
COMMENT ON COLUMN farms.farm_fair_market_values.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_fair_market_values.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.';
COMMENT ON COLUMN farms.farm_fair_market_values.percent_variance IS E'PERCENT VARIANCE is the amount the AVERAGE PRICE might vary in a month.';
COMMENT ON COLUMN farms.farm_fair_market_values.period IS E'PERIOD is a numeric identifier for a month. 1 = January, 12 = December.';
COMMENT ON COLUMN farms.farm_fair_market_values.program_year IS E'PROGRAM YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_fair_market_values.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_fair_market_values.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_fair_market_values.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_fair_market_values.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_fair_market_values.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_fair_market_values.url_id IS E'URL ID is a foreign key to FARM_URLS.';
CREATE INDEX farm_fmv_farm_cuc_fk_i ON farms.farm_fair_market_values (crop_unit_code);
CREATE INDEX farm_fmv_farm_ic_fk_i ON farms.farm_fair_market_values (inventory_item_code);
CREATE INDEX farm_fmv_farm_mc_fk_i ON farms.farm_fair_market_values (municipality_code);
CREATE INDEX farm_fmv_import_i ON farms.farm_fair_market_values (program_year, period, inventory_item_code, municipality_code, crop_unit_code);
CREATE INDEX farm_fmv_year_i ON farms.farm_fair_market_values (program_year);
CREATE INDEX farm_fmv_farm_url_fk_i ON farms.farm_fair_market_values (url_id);
ALTER TABLE farms.farm_fair_market_values ADD CONSTRAINT farm_fmv_pk PRIMARY KEY (fair_market_value_id);
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN fair_market_value_id SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN period SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN average_price SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN percent_variance SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN municipality_code SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_fair_market_values ALTER COLUMN when_created SET NOT NULL;
