CREATE TABLE farms.farm_tip_income_ranges(
    tip_income_range_id          numeric(10, 0)    NOT NULL,
    range_low                    numeric(13, 2)    NOT NULL,
    range_high                   numeric(13, 2)    NOT NULL,
    minimum_group_count          numeric(3, 0)     NOT NULL,
    tip_farm_type_3_lookup_id    numeric(10, 0),
    tip_farm_type_2_lookup_id    numeric(10, 0),
    tip_farm_type_1_lookup_id    numeric(10, 0),
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                  varchar(30)       NOT NULL,
    when_created                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                  varchar(30),
    when_updated                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_income_range_id IS 'TIP INCOME RANGE ID is a surrogate unique identifier for a TIP INCOME RANGE.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.range_low IS 'RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.range_high IS 'RANGE HIGH is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.minimum_group_count IS 'MINIMUM GROUP COUNT is the minimum number of farms required before any farms can be assigned to a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_3_lookup_id IS 'TIP FARM TYPE 3 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 3 LOOKUP.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_2_lookup_id IS 'TIP FARM TYPE 2 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 2 LOOKUP.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.tip_farm_type_1_lookup_id IS 'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_income_ranges.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_tip_income_ranges IS 'TIP INCOME RANGE contains the low and high ranges of a grouping for a TIP BENCHMARK YEAR.'
;


CREATE UNIQUE INDEX uk_tir_tft3l_tft2l_tft1l_rh ON farms.farm_tip_income_ranges(tip_farm_type_3_lookup_id, tip_farm_type_2_lookup_id, tip_farm_type_1_lookup_id, range_high)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tir_tft3l_tft2l_tft1l_rl ON farms.farm_tip_income_ranges(tip_farm_type_3_lookup_id, tip_farm_type_2_lookup_id, tip_farm_type_1_lookup_id, range_low)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_tip_income_ranges ADD 
    CONSTRAINT pk_tir PRIMARY KEY (tip_income_range_id) USING INDEX TABLESPACE pg_default 
;
