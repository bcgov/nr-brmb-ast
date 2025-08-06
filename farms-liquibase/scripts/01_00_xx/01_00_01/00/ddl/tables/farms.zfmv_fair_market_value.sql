CREATE TABLE farms.farm_zfmv_fair_market_values(
    line_number            numeric(10, 0)    NOT NULL,
    program_year           numeric(4, 0)     NOT NULL,
    period                 numeric(2, 0)     NOT NULL,
    average_price          numeric(15, 4)    NOT NULL,
    percent_variance       numeric(7, 4)     NOT NULL,
    municipality_code      varchar(10)       NOT NULL,
    crop_unit_code         varchar(10)       NOT NULL,
    inventory_item_code    varchar(240)      NOT NULL,
    revision_count         numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created            varchar(30)       NOT NULL,
    when_created            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated            varchar(30),
    when_updated            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.line_number IS 'LINE NUMBER is the number of a line in a CSV file. Every time a FMV CSV file is loaded, all the FMV staging data will be deleted. Also used for generating errors to the user about data problems in the input file.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.program_year IS 'PROGRAM YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.period IS 'PERIOD is a numeric identifier for a month. 1 = January, 12 = December.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.average_price IS 'AVERAGE PRICE is the average market price obtained for a crop for a given month.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.percent_variance IS 'PERCENT VARIANCE is the amount the AVERAGE PRICE might vary in a month.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FAIR MARKET VALUE.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zfmv_fair_market_values.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


ALTER TABLE farms.farm_zfmv_fair_market_values ADD 
    CONSTRAINT pk_zfmv PRIMARY KEY (line_number) USING INDEX TABLESPACE pg_default 
;
