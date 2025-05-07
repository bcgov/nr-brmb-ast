CREATE TABLE fair_market_value(
    fair_market_value_id    numeric(10, 0)    NOT NULL,
    program_year            numeric(4, 0)     NOT NULL,
    period                  numeric(2, 0)     NOT NULL,
    average_price           numeric(13, 2)    NOT NULL,
    percent_variance        numeric(5, 2)     NOT NULL,
    expiry_date             date,
    inventory_item_code     varchar(10)       NOT NULL,
    municipality_code       varchar(10)       NOT NULL,
    crop_unit_code          varchar(10)       NOT NULL,
    revision_count          numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user             varchar(30)       NOT NULL,
    create_date             timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user             varchar(30),
    update_date             timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN fair_market_value.fair_market_value_id IS 'FAIR MARKET VALUE ID is a surrogate unique identifier for FAIR MARKET VALUEs.'
;
COMMENT ON COLUMN fair_market_value.program_year IS 'PROGRAM YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN fair_market_value.period IS 'PERIOD is a numeric identifier for a month. 1 = January, 12 = December.'
;
COMMENT ON COLUMN fair_market_value.average_price IS 'AVERAGE PRICE is the average market price obtained for a crop for a given month.'
;
COMMENT ON COLUMN fair_market_value.percent_variance IS 'PERCENT VARIANCE is the amount the AVERAGE PRICE might vary in a month.'
;
COMMENT ON COLUMN fair_market_value.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN fair_market_value.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN fair_market_value.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.'
;
COMMENT ON COLUMN fair_market_value.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN fair_market_value.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN fair_market_value.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN fair_market_value.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN fair_market_value.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN fair_market_value.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE fair_market_value IS 'FAIR MARKET VALUE is the market price for selling a certain amount of a crop. The price varies between municipalities and between months of the year. Within a month the price can vary by a certain percentage. The FAIR MARKET VALUE is used as a basis for comparison with a producer''s reported income and expenses.'
;


CREATE INDEX ix_fmv_cuc ON fair_market_value(crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_fmv_iic ON fair_market_value(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_fmv_mc ON fair_market_value(municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_fmv_py_p_iic_mc_cuc ON fair_market_value(program_year, period, inventory_item_code, municipality_code, crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_fmv_py ON fair_market_value(program_year)
 TABLESPACE pg_default
;
