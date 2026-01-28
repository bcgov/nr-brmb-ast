-- create new table
CREATE TABLE farms.farm_zivpr_iv_premium_rates_new (
    line_number bigint NOT NULL,
    program_year smallint NOT NULL,
    inventory_item_code varchar(10) NOT NULL,
    insurable_value decimal(13,3) NOT NULL,
    premium_rate decimal(13,4) NOT NULL,
    file_location varchar(256),
    revision_count integer NOT NULL DEFAULT 1,
    who_created varchar(30) NOT NULL,
    when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
    who_updated varchar(30),
    when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.insurable_value IS E'INSURABLE VALUE is the dollar amount of insurance per unit that the AgriStability program requires the participant to carry.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.line_number IS E'LINE NUMBER is the number of a line in a CSV file. Everytime an IVPR CSV file is loaded, all the IVPR staging data will be deleted. Also used for generating errors to the user about data problems in the input file.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.premium_rate IS E'PREMIUM RATE is the main portion of the premium the participant pays for insurance of their production it is the percentage of the total dollar value they have insured for this INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.program_year IS E'PROGRAM YEAR is the PROGRAM YEAR this data pertains to.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_zivpr_iv_premium_rates_new.file_location IS E'FILE LOCATION is the URL of the file in SharePoint Online.';

-- populate new table
INSERT INTO farms.farm_zivpr_iv_premium_rates_new (
    line_number,
    program_year,
    inventory_item_code,
    insurable_value,
    premium_rate,
    file_location,
    revision_count,
    who_created,
    when_created,
    who_updated,
    when_updated
)
SELECT line_number,
       program_year,
       inventory_item_code,
       insurable_value,
       premium_rate,
       null,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated
FROM farms.farm_zivpr_iv_premium_rates;

-- drop indexes

-- drop constraints
ALTER TABLE farms.farm_zivpr_iv_premium_rates DROP CONSTRAINT IF EXISTS farm_zivpr_pk;

-- drop old table
DROP TABLE farms.farm_zivpr_iv_premium_rates;

-- rename new table
ALTER TABLE farms.farm_zivpr_iv_premium_rates_new RENAME TO farm_zivpr_iv_premium_rates;

-- add indexes

-- add constraints
ALTER TABLE farms.farm_zivpr_iv_premium_rates ADD CONSTRAINT farm_zivpr_pk PRIMARY KEY (line_number);
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN line_number SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN insurable_value SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN premium_rate SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_zivpr_iv_premium_rates ALTER COLUMN when_created SET NOT NULL;

-- grant permissions
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE ON farms.farm_zivpr_iv_premium_rates TO "app_farms_rest_proxy";
