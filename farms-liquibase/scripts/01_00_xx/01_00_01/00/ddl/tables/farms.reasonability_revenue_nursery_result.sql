CREATE TABLE farms.farm_rsn_rev_nursery_results(
    rsn_rev_nursery_result_id    numeric(10, 0)    NOT NULL,
    nursery_pass_ind                     varchar(1)        NOT NULL,
    reported_revenue                           numeric(13, 2),
    expected_revenue                           numeric(25, 2),
    revenue_variance                           numeric(16, 3),
    revenue_variance_limit                     numeric(16, 3)    NOT NULL,
    reasonability_test_result_id               numeric(10, 0)    NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                varchar(30)       NOT NULL,
    when_created                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                varchar(30),
    when_updated                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.rsn_rev_nursery_result_id IS 'REASONABILITY REVENUE NURSERY RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE NURSERY RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.nursery_pass_ind IS 'NURSERY PASS INDICATOR denotes whether Revenue Risk - Nursery subtest passed.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.reported_revenue IS 'REPORTED REVENUE is the total revenue reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: NURSERY.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.expected_revenue IS 'EXPECTED REVENUE is the total revenue expected based on REPORTED INVENTORY items with COMMODITY TYPE CODE: NURSERY.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revenue_variance IS 'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revenue_variance_limit IS 'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_results.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_rev_nursery_results IS 'REASONABILITY REVENUE NURSERY RESULT contains the results of the Revenue Risk - Nursery Subtest of the scenario.'
;


CREATE UNIQUE INDEX uk_rrnr_rtri ON farms.farm_rsn_rev_nursery_results(reasonability_test_result_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_rev_nursery_results ADD 
    CONSTRAINT pk_rrnr PRIMARY KEY (rsn_rev_nursery_result_id) USING INDEX TABLESPACE pg_default 
;
