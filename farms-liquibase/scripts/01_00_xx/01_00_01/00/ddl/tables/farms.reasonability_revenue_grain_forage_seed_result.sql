CREATE TABLE farms.reasonability_revenue_grain_forage_seed_result(
    reasonability_revenue_grain_forage_seed_result_id    numeric(10, 0)    NOT NULL,
    reported_revenue                                     numeric(13, 2),
    expected_revenue                                     numeric(25, 2),
    revenue_variance                                     numeric(16, 3),
    revenue_within_limit_indicator                       varchar(1),
    line_item                                            numeric(4, 0)     NOT NULL,
    reasonability_test_result_id                         numeric(10, 0)    NOT NULL,
    revision_count                                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                          varchar(30)       NOT NULL,
    create_date                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                                          varchar(30),
    update_date                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.reasonability_revenue_grain_forage_seed_result_id IS 'REASONABILITY REVENUE GRAIN FORAGE SEED RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE GRAIN FORAGE SEED RESULT.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.reported_revenue IS 'REPORTED REVENUE is the total income for this LINE ITEM. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.expected_revenue IS 'EXPECTED REVENUE is the amount of revenue based on inventory sold and reported prices.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.revenue_variance IS 'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.revenue_within_limit_indicator IS 'REVENUE WITHIN LIMIT INDICATOR indicates whether REPORTED REVENUE is within the configured limit of the EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_grain_forage_seed_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.reasonability_revenue_grain_forage_seed_result IS 'REASONABILITY REVENUE GRAIN FORAGE SEED RESULT contains the calculated amounts for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by LINE ITEM.'
;


CREATE INDEX ix_rrgfsr_rtri ON farms.reasonability_revenue_grain_forage_seed_result(reasonability_test_result_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rrgfsr_rtri_li ON farms.reasonability_revenue_grain_forage_seed_result(reasonability_test_result_id, line_item)
 TABLESPACE pg_default
;

ALTER TABLE farms.reasonability_revenue_grain_forage_seed_result ADD 
    CONSTRAINT pk_rrgfsr PRIMARY KEY (reasonability_revenue_grain_forage_seed_result_id) USING INDEX TABLESPACE pg_default 
;
