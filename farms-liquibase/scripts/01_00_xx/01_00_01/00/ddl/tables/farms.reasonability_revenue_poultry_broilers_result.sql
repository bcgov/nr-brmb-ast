CREATE TABLE reasonability_revenue_poultry_broilers_result(
    reasonability_revenue_poultry_broilers_result_id    numeric(10, 0)    NOT NULL,
    has_poultry_broilers_indicator                      varchar(1)        NOT NULL,
    poultry_broiler_pass_indicator                      varchar(1)        NOT NULL,
    has_chickens_indicator                              varchar(1)        NOT NULL,
    chicken_pass_indicator                              varchar(1)        NOT NULL,
    chicken_kg_produced                                 numeric(13, 3),
    chicken_average_weight_kg                           numeric(16, 3)    NOT NULL,
    chicken_expected_birds_sold                         numeric(14, 3),
    chicken_price_per_bird                              numeric(13, 2),
    chicken_expected_revenue                            numeric(25, 2),
    chicken_reported_revenue                            numeric(13, 2),
    chicken_revenue_variance                            numeric(16, 3),
    chicken_revenue_variance_limit                      numeric(16, 3)    NOT NULL,
    has_turkeys_indicator                               varchar(1)        NOT NULL,
    turkey_pass_indicator                               varchar(1)        NOT NULL,
    turkey_kg_produced                                  numeric(13, 3),
    turkey_average_weight_kg                            numeric(16, 3)    NOT NULL,
    turkey_expected_birds_sold                          numeric(14, 3),
    turkey_price_per_bird                               numeric(13, 2),
    turkey_expected_revenue                             numeric(25, 2),
    turkey_reported_revenue                             numeric(13, 2),
    turkey_revenue_variance                             numeric(16, 3),
    turkey_revenue_variance_limit                       numeric(16, 3)    NOT NULL,
    reasonability_test_result_id                        numeric(10, 0)    NOT NULL,
    revision_count                                      numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                         varchar(30)       NOT NULL,
    create_date                                         timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                         varchar(30),
    update_date                                         timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.reasonability_revenue_poultry_broilers_result_id IS 'REASONABILITY REVENUE POULTRY BROILERS RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE POULTRY BROILERS RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.has_poultry_broilers_indicator IS 'HAS POULTRY BROILERS INDICATOR denotes whether this operation has poultry broilers.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.poultry_broiler_pass_indicator IS 'POULTRY BROILER PASS INDICATOR denotes whether Revenue Risk - Poultry - Broilers subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.has_chickens_indicator IS 'HAS CHICKENS INDICATOR denotes whether the this operation has broiler chickens.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_pass_indicator IS 'CHICKEN PASS INDICATOR denotes whether the chicken part of this subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_kg_produced IS 'CHICKEN KG PRODUCED is the Kilograms of chicken produced. This is the productive units from PRODUCTIVE UNIT CAPACITY.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_average_weight_kg IS 'CHICKEN AVERAGE WEIGHT KG is the configured average weight of a broiler chicken in Kilograms.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_expected_birds_sold IS 'CHICKEN EXPECTED BIRDS SOLD is the number of chickens we expect the producer to have sold based on their productive units (kg produced) and the average weight of a chicken.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_price_per_bird IS 'CHICKEN PRICE PER BIRD is the FMV Price End for a chicken based on the fiscal year end of the operation and the FMV price for inventory code 7681.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_expected_revenue IS 'CHICKEN EXPECTED REVENUE is the expected revenue from broiler chickens based on the CHICKEN EXPECTED BIRDS SOLD and CHICKEN PRICE PER BIRD.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_reported_revenue IS 'CHICKEN REPORTED REVENUE is the revenue reported for chicken LINE ITEMs in FARM REPORTED INCOME EXPENSE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_revenue_variance IS 'CHICKEN REVENUE VARIANCE is the percent difference between CHICKEN REPORTED REVENUE and CHICKEN EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.chicken_revenue_variance_limit IS 'CHICKEN REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between CHICKEN REPORTED REVENUE and CHICKEN EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.has_turkeys_indicator IS 'HAS TURKEYS INDICATOR denotes whether the this operation has broiler turkeys.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_pass_indicator IS 'TURKEY PASS INDICATOR denotes whether the turkey part of this subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_kg_produced IS 'TURKEY KG PRODUCED is the Kilograms of turkey produced. This is the productive units from PRODUCTIVE UNIT CAPACITY.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_average_weight_kg IS 'TURKEY AVERAGE WEIGHT KG is the configured average weight of a broiler turkey in Kilograms.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_expected_birds_sold IS 'TURKEY EXPECTED BIRDS SOLD is the number of turkeys we expect the producer to have sold based on their productive units (kg produced) and the average weight of a turkey.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_price_per_bird IS 'TURKEY PRICE PER BIRD is the FMV Price End for a turkey based on the fiscal year end of the operation and the FMV price for inventory code 7681.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_expected_revenue IS 'TURKEY EXPECTED REVENUE is the expected revenue from broiler turkeys based on the TURKEY EXPECTED BIRDS SOLD and TURKEY PRICE PER BIRD.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_reported_revenue IS 'TURKEY REPORTED REVENUE is the revenue reported for turkey LINE ITEMs in FARM REPORTED INCOME EXPENSE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_revenue_variance IS 'TURKEY REVENUE VARIANCE is the percent difference between TURKEY REPORTED REVENUE and TURKEY EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.turkey_revenue_variance_limit IS 'TURKEY REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between TURKEY REPORTED REVENUE and TURKEY EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_broilers_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reasonability_revenue_poultry_broilers_result IS 'REASONABILITY REVENUE POULTRY BROILERS RESULT contains the results of the Revenue Risk - Poultry - Broilers Subtest of the scenario.'
;


CREATE UNIQUE INDEX uk_rrpbr_rtri ON reasonability_revenue_poultry_broilers_result(reasonability_test_result_id)
 TABLESPACE pg_default
;

ALTER TABLE reasonability_revenue_poultry_broilers_result ADD 
    CONSTRAINT pk_rrpbr PRIMARY KEY (reasonability_revenue_poultry_broilers_result_id) USING INDEX TABLESPACE pg_default 
;
