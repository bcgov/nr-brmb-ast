CREATE TABLE reasonability_revenue_fruit_vegetables_result(
    reasonability_revenue_fruit_vegetables_result_id    numeric(10, 0)    NOT NULL,
    reported_revenue                                    numeric(13, 2),
    quantity_produced                                   numeric(19, 3),
    expected_price                                      numeric(13, 2),
    expected_revenue                                    numeric(25, 2),
    revenue_variance                                    numeric(19, 3),
    revenue_variance_limit                              numeric(16, 3),
    revenue_within_limit_indicator                      varchar(1),
    fruit_vegetable_type_code                           varchar(10)       NOT NULL,
    crop_unit_code                                      varchar(10)       NOT NULL,
    reasonability_test_result_id                        numeric(10, 0)    NOT NULL,
    revision_count                                      numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                         varchar(30)       NOT NULL,
    create_date                                         timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                         varchar(30),
    update_date                                         timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.reasonability_revenue_fruit_vegetables_result_id IS 'REASONABILITY REVENUE FRUIT VEGETABLES RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE FRUIT VEGETABLES RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.reported_revenue IS 'REPORTED REVENUE is the total income for this FRUIT VEGETABLE TYPE CODE. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.quantity_produced IS 'QUANTITY PRODUCED is the quantity produced from REPORTED INVENTORY.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.expected_price IS 'EXPECTED PRICE is the benchmark price per CROP UNIT CODE that the farm could be expected to earn under normal circumstances.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.expected_revenue IS 'EXPECTED REVENUE is the PRODUCTION POUNDS multiplied by the EXPECTED PRICE PER POUND.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.revenue_variance IS 'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.revenue_variance_limit IS 'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.revenue_within_limit_indicator IS 'REVENUE WITHIN LIMIT INDICATOR indicates whether REPORTED REVENUE is within the configured limit of the EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.fruit_vegetable_type_code IS 'FRUIT VEGETABLE TYPE CODE is a unique code for the object FRUIT VEGETABLE TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_fruit_vegetables_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reasonability_revenue_fruit_vegetables_result IS 'REASONABILITY REVENUE FRUIT VEGETABLES RESULT contains the calculated results for the Reasonability Test: Fruits and Vegetables Revenue Test run against the scenario, by FRUIT VEGETABLE TYPE CODE.'
;


CREATE INDEX ix_rrfvr_cuc ON reasonability_revenue_fruit_vegetables_result(crop_unit_code)
;
CREATE INDEX ix_rrfvr_fvtc ON reasonability_revenue_fruit_vegetables_result(fruit_vegetable_type_code)
;
CREATE INDEX ix_rrfvr_rtri ON reasonability_revenue_fruit_vegetables_result(reasonability_test_result_id)
;
CREATE UNIQUE INDEX uk_rrfvr_rtri_fvtc ON reasonability_revenue_fruit_vegetables_result(reasonability_test_result_id, fruit_vegetable_type_code)
;
