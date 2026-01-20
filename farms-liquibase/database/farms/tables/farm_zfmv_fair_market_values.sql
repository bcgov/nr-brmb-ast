CREATE TABLE farms.farm_zfmv_fair_market_values (
	line_number bigint NOT NULL,
	program_year smallint NOT NULL,
	period smallint NOT NULL,
	average_price decimal(15,4) NOT NULL,
	percent_variance decimal(7,4) NOT NULL,
	municipality_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	inventory_item_code varchar(240) NOT NULL,
	file_location varchar(256),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.average_price IS E'AVERAGE PRICE is the average market price obtained for a crop for a given month.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.line_number IS E'LINE NUMBER is the number of a line in a CSV file. Everytime a FMV CSV file is loaded, all the FMV staging data will be deleted. Also used for generating errors to the user about data problems in the input file.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the FAIR MARKET VALUE.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.percent_variance IS E'PERCENT VARIANCE is the amount the AVERAGE PRICE might vary in a month.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.period IS E'PERIOD is a numeric identifier for a month. 1 = January, 12 = December.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.program_year IS E'PROGRAM YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.file_location IS E'FILE LOCATION is the URL of the file in SharePoint Online.';
ALTER TABLE farms.farm_zfmv_fair_market_values ADD CONSTRAINT farm_zfmv_pk PRIMARY KEY (line_number);
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN line_number SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN period SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN average_price SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN percent_variance SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN municipality_code SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_zfmv_fair_market_values ALTER COLUMN when_created SET NOT NULL;
