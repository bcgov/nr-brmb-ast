CREATE TABLE tip_income_range(
    tip_income_range_id          numeric(10, 0)    NOT NULL,
    range_low                    numeric(13, 2)    NOT NULL,
    range_high                   numeric(13, 2)    NOT NULL,
    minimum_group_count          numeric(3, 0)     NOT NULL,
    tip_farm_type_3_lookup_id    numeric(10, 0),
    tip_farm_type_2_lookup_id    numeric(10, 0),
    tip_farm_type_1_lookup_id    numeric(10, 0),
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_income_range.tip_income_range_id IS 'TIP INCOME RANGE ID is a surrogate unique identifier for a TIP INCOME RANGE.'
;
COMMENT ON COLUMN tip_income_range.range_low IS 'RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_income_range.range_high IS 'RANGE HIGH is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_income_range.minimum_group_count IS 'MINIMUM GROUP COUNT is the minimum number of farms required before any farms can be assigned to a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_income_range.tip_farm_type_3_lookup_id IS 'TIP FARM TYPE 3 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 3 LOOKUP.'
;
COMMENT ON COLUMN tip_income_range.tip_farm_type_2_lookup_id IS 'TIP FARM TYPE 2 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 2 LOOKUP.'
;
COMMENT ON COLUMN tip_income_range.tip_farm_type_1_lookup_id IS 'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.'
;
COMMENT ON COLUMN tip_income_range.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_income_range.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_income_range.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_income_range.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_income_range.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_income_range IS 'TIP INCOME RANGE contains the low and high ranges of a grouping for a TIP BENCHMARK YEAR.'
;


CREATE UNIQUE INDEX uk_tir_tft3l_tft2l_tft1l_rh ON tip_income_range(tip_farm_type_3_lookup_id, tip_farm_type_2_lookup_id, tip_farm_type_1_lookup_id, range_high)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tir_tft3l_tft2l_tft1l_rl ON tip_income_range(tip_farm_type_3_lookup_id, tip_farm_type_2_lookup_id, tip_farm_type_1_lookup_id, range_low)
 TABLESPACE pg_default
;
