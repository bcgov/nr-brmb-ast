CREATE TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts(
    rsn_rev_g_f_fs_incm_rslt_id    numeric(10, 0)    NOT NULL,
    reported_revenue                                     numeric(13, 2),
    expected_revenue                                     numeric(25, 2),
    revenue_variance                                     numeric(16, 3),
    revenue_within_limit_ind                       varchar(1),
    line_item                                            numeric(4, 0)     NOT NULL,
    reasonability_test_result_id                         numeric(10, 0)    NOT NULL,
    revision_count                                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                          varchar(30)       NOT NULL,
    when_created                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                          varchar(30),
    when_updated                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.rsn_rev_g_f_fs_incm_rslt_id IS 'REASONABILITY REVENUE GRAIN FORAGE SEED RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE GRAIN FORAGE SEED RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.reported_revenue IS 'REPORTED REVENUE is the total income for this LINE ITEM. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.expected_revenue IS 'EXPECTED REVENUE is the amount of revenue based on inventory sold and reported prices.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_variance IS 'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_within_limit_ind IS 'REVENUE WITHIN LIMIT INDICATOR indicates whether REPORTED REVENUE is within the configured limit of the EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_g_f_fs_incm_rslts.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts IS 'REASONABILITY REVENUE GRAIN FORAGE SEED RESULT contains the calculated amounts for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by LINE ITEM.'
;


CREATE INDEX ix_rrgfsr_rtri ON farms.farm_rsn_rev_g_f_fs_incm_rslts(reasonability_test_result_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rrgfsr_rtri_li ON farms.farm_rsn_rev_g_f_fs_incm_rslts(reasonability_test_result_id, line_item)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_rev_g_f_fs_incm_rslts ADD 
    CONSTRAINT pk_rrgfsr PRIMARY KEY (rsn_rev_g_f_fs_incm_rslt_id) USING INDEX TABLESPACE pg_default 
;
