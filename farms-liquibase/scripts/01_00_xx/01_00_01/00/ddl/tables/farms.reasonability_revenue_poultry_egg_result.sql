CREATE TABLE reasonability_revenue_poultry_egg_result(
    reasonability_revenue_poultry_egg_result_id    numeric(10, 0)    NOT NULL,
    has_poultry_eggs_indicator                     varchar(1)        NOT NULL,
    poultry_eggs_pass_indicator                    varchar(1)        NOT NULL,
    consumption_pass_indicator                     varchar(1)        NOT NULL,
    consumption_layers                             numeric(13, 3),
    consumption_avg_eggs_per_layer                 numeric(5, 1)     NOT NULL,
    consumption_eggs_total                         numeric(18, 3),
    consumption_eggs_dozen_sold                    numeric(17, 3),
    consumption_price_per_dozen                    numeric(13, 2),
    consumption_expected_revenue                   numeric(30, 2),
    consumption_reported_revenue                   numeric(13, 2),
    consumption_revenue_variance                   numeric(16, 3),
    consumption_revenue_variance_limit             numeric(16, 3)    NOT NULL,
    hatching_pass_indicator                        varchar(1)        NOT NULL,
    hatching_layers                                numeric(13, 3),
    hatching_average_eggs_per_layer                numeric(5, 1)     NOT NULL,
    hatching_eggs_total                            numeric(18, 3),
    hatching_eggs_dozen_sold                       numeric(17, 3),
    hatching_price_per_dozen                       numeric(13, 2),
    hatching_expected_revenue                      numeric(30, 2),
    hatching_reported_revenue                      numeric(13, 2),
    hatching_revenue_variance                      numeric(16, 3),
    hatching_revenue_variance_limt                 numeric(16, 3)    NOT NULL,
    reasonability_test_result_id                   numeric(10, 0)    NOT NULL,
    revision_count                                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                    varchar(30)       NOT NULL,
    create_date                                    timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                    varchar(30),
    update_date                                    timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.reasonability_revenue_poultry_egg_result_id IS 'REASONABILITY REVENUE POULTRY EGG RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE POULTRY EGG RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.has_poultry_eggs_indicator IS 'HAS POULTRY EGGS INDICATOR denotes whether this operation has poultry eggs.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.poultry_eggs_pass_indicator IS 'POULTRY EGGS PASS INDICATOR denotes whether Revenue Risk - Poultry - Eggs subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_pass_indicator IS 'CONSUMPTION PASS INDICATOR denotes whether the consumption part of this subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_layers IS 'CONSUMPTION LAYERS is the chickens (layers) laying eggs for consumption. This is the productive units from PRODUCTIVE UNIT CAPACITY.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_avg_eggs_per_layer IS 'CONSUMPTION AVERAGE EGGS PER LAYER is the configured average number of eggs laid for hatching per chicken (layer) per year.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_eggs_total IS 'CONSUMPTION EGGS TOTAL is the number of eggs for consumption that we expect the producer to have produced based on their productive units (layers) and the average eggs produced per layer.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_eggs_dozen_sold IS 'CONSUMPTION EGGS DOZEN SOLD is the number of dozen eggs for consumption that we expect the producer to have sold based on CONSUMPTION EGGS TOTAL.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_price_per_dozen IS 'CONSUMPTION PRICE PER DOZEN is the FMV Price End for a dozen eggs for consumption based on the fiscal year end of the operation and the FMV price for inventory code 7664.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_expected_revenue IS 'CONSUMPTION EXPECTED REVENUE is the expected revenue from eggs for consumption based on the CONSUMPTION EXPECTED DOZEN SOLD and CONSUMPTION PRICE PER DOZEN.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_reported_revenue IS 'CONSUMPTION REPORTED REVENUE is the revenue reported for consumption LINE ITEMs in FARM REPORTED INCOME EXPENSE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_revenue_variance IS 'CONSUMPTION REVENUE VARIANCE is the percent difference between CONSUMPTION REPORTED REVENUE and CONSUMPTION EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.consumption_revenue_variance_limit IS 'CONSUMPTION REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between CONSUMPTION REPORTED REVENUE and CONSUMPTION EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_pass_indicator IS 'HATCHING PASS INDICATOR denotes whether the hatching part of this subtest passed.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_layers IS 'HATCHING LAYERS is the chickens (layers) laying eggs for hatching. This is the productive units from PRODUCTIVE UNIT CAPACITY.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_average_eggs_per_layer IS 'HATCHING AVERAGE EGGS PER LAYER is the configured average number of eggs laid for hatching per chicken (layer) per year.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_eggs_total IS 'HATCHING EGGS TOTAL is the number of eggs for hatching that we expect the producer to have produced based on their productive units (layers) and the average eggs produced per layer.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_eggs_dozen_sold IS 'HATCHING EGGS DOZEN SOLD is the number of dozen eggs for consumption that we expect the producer to have sold based on HATCHING EGGS TOTAL.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_price_per_dozen IS 'HATCHING PRICE PER DOZEN is the FMV Price End for a dozen eggs for hatching based on the fiscal year end of the operation and the FMV price for inventory code 7663.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_expected_revenue IS 'HATCHING EXPECTED REVENUE is the expected revenue from eggs for hatching based on the HATCHING EXPECTED DOZEN SOLD and HATCHING PRICE PER DOZEN.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_reported_revenue IS 'HATCHING REPORTED REVENUE is the revenue reported for hatching LINE ITEMs in FARM REPORTED INCOME EXPENSE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_revenue_variance IS 'HATCHING REVENUE VARIANCE is the percent difference between HATCHING REPORTED REVENUE and HATCHING EXPECTED REVENUE.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.hatching_revenue_variance_limt IS 'HATCHING REVENUE VARIANCE LIMT is the configured limit for the percentage difference between HATCHING REPORTED REVENUE and HATCHING EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_poultry_egg_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reasonability_revenue_poultry_egg_result IS 'REASONABILITY REVENUE POULTRY EGG RESULT contains the results of the Revenue Risk - Poultry - Eggs Subtest of the scenario.'
;


CREATE UNIQUE INDEX uk_rrper_rtri ON reasonability_revenue_poultry_egg_result(reasonability_test_result_id)
;
