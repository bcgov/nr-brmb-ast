CREATE TABLE farms.farm_reasonabilty_test_results(
    reasonability_test_result_id                               numeric(10, 0)    NOT NULL,
    fresh_ind                                            varchar(1)        NOT NULL,
    generated_date                                             date              NOT NULL,
    forage_consumer_capacity                                   numeric(19, 3),
    benefit_risk_pass_ind                                varchar(1)        NOT NULL,
    program_year_margin                                        numeric(13, 2)    NOT NULL,
    total_benefit                                              numeric(13, 2)    NOT NULL,
    benchmark_margin                                           numeric(25, 2),
    benchmark_margin_deductible                                numeric(13, 3)    NOT NULL,
    benchmark_margin_less_deductbl                           numeric(25, 2),
    benchmark_margin_less_py_marg                  numeric(25, 2),
    benchmark_margin_payout_level                              numeric(13, 3)    NOT NULL,
    benefit_benchmark_befor_combnd                          numeric(25, 2)    NOT NULL,
    combined_benefit_percent                                   numeric(4, 3),
    benefit_benchmark                                          numeric(25, 2)    NOT NULL,
    benefit_risk_variance                                      numeric(25, 3),
    benefit_risk_variance_limit                                numeric(16, 3)    NOT NULL,
    margin_pass_ind                                      varchar(1)        NOT NULL,
    adjusted_reference_margin                                  numeric(13, 2),
    adj_reference_margin_variance                         numeric(16, 3),
    adj_reference_margin_vrinc_lmt                   numeric(16, 3)    NOT NULL,
    within_lmt_prct_ref_margin_ind         varchar(1)        NOT NULL,
    margin_industry_average                                    numeric(13, 2)    NOT NULL,
    margin_industry_avg_variance                           numeric(16, 3),
    margin_industry_average_limit                              numeric(16, 3)    NOT NULL,
    margin_wthn_lmt_indtry_avg_ind             varchar(1)        NOT NULL,
    structural_change_pass_ind                           varchar(1)        NOT NULL,
    production_marg_accr_adjs                      numeric(16, 2)    NOT NULL,
    ratio_adj_reference_margin                          numeric(13, 2)    NOT NULL,
    additive_adj_reference_margin                       numeric(13, 2)    NOT NULL,
    ratio_additive_ref_marg_diff                 numeric(13, 2)    NOT NULL,
    ratio_add_diff_dollar_limit                     numeric(13, 2)    NOT NULL,
    within_dlr_limit_ratio_add_ind               varchar(1)        NOT NULL,
    additive_division_ratio                                    numeric(16, 3),
    additive_division_ratio_limit                              numeric(16, 3)    NOT NULL,
    within_limit_addtiv_divisn_ind                   varchar(1)        NOT NULL,
    structual_change_method_to_use                             varchar(10)       NOT NULL,
    farm_size_ratio_limit                                      numeric(8, 3)     NOT NULL,
    farm_size_rtios_within_lmt_ind                   varchar(1)        NOT NULL,
    revenue_risk_pass_ind                                varchar(1),
    revenue_risk_fruit_vg_pass_ind               varchar(1),
    revenue_rsk_grain_frg_pass_ind                   varchar(1),
    revenue_rsk_grain_forage_limit                            numeric(13, 2),
    expense_indtry_av_cmp_pass_ind          varchar(1)        NOT NULL,
    expense_accr_adjs_indtry_av_cm    numeric(16, 2)    NOT NULL,
    expense_industry_average                                   numeric(13, 2)    NOT NULL,
    expense_industry_avg_variance                          numeric(16, 3),
    expense_industry_average_limit                             numeric(16, 3)    NOT NULL,
    expense_ref_year_comp_pass_ind           varchar(1),
    expense_accr_adjs                                   numeric(16, 2)    NOT NULL,
    expenses_aft_str_ch_yr_minus_1              numeric(13, 2),
    expenses_aft_str_ch_yr_minus_2              numeric(13, 2),
    expenses_aft_str_ch_yr_minus_3              numeric(13, 2),
    expenses_aft_str_ch_yr_minus_4              numeric(13, 2),
    expenses_aft_str_ch_yr_minus_5              numeric(13, 2),
    expenses_aft_str_ch_ref_yr_avg    numeric(13, 2),
    expenses_aft_str_ch_variance                  numeric(16, 2),
    expenses_aft_str_ch_varian_lmt            numeric(16, 2),
    production_pass_ind                                  varchar(1),
    production_forage_fs_pass_ind                      varchar(1),
    prod_forage_qty_produced_limit                  numeric(16, 3),
    production_grains_pass_ind                           varchar(1),
    prod_grain_qty_produced_limit                   numeric(16, 3),
    production_fruit_veg_pass_ind                 varchar(1),
    prod_fruit_veg_qty_prodcd_limt        numeric(16, 3),
    agristability_scenario_id                                  numeric(10, 0)    NOT NULL,
    revision_count                                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                                varchar(30)       NOT NULL,
    when_created                                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                                varchar(30),
    when_updated                                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_reasonabilty_test_results.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.fresh_ind IS 'FRESH INDICATOR is used to determine if the test results are fresh. This means that they were run with the most recent changes to scenario which could change their results.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.generated_date IS 'GENERATED DATE identifies the date that the reasonability test result was generated.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.forage_consumer_capacity IS 'FORAGE CONSUMER CAPACITY is the amount of forage inventory potentially consumed by (fed out to) cattle. The actual amount will be less if the capacity is greater than the the inventory.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_pass_ind IS 'BENEFIT RISK PASS INDICATOR denotes whether Benefit Risk Assessment passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.program_year_margin IS 'PROGRAM YEAR MARGIN is determined by the Income less allowable expenses.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.total_benefit IS 'TOTAL BENEFIT is the total calculated benefit prior to deductions.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin IS 'BENCHMARK MARGIN is the total industry margin calculated as the PRODUCTIVE UNIT AMOUNT multiplied by the BPU 5-year average (taking missing years and Lead/Lag into account).'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_deductible IS 'BENCHMARK MARGIN DEDUCTIBLE is the deductible percentage of the margin not covered.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_less_deductbl IS 'BENCHMARK MARGIN LESS DEDUCTIBLE is BENCHMARK MARGIN minus the deductible amount.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_less_py_marg IS 'BENCHMARK MARGIN LESS PROGRAM YEAR MARGIN is BENCHMARK MARGIN LESS DEDUCTIBLE minus the PROGRAM YEAR MARGIN.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_payout_level IS 'BENCHMARK MARGIN PAYOUT LEVEL is the percentage of BENCHMARK MARGIN LESS PROGRAM YEAR MARGIN that would be paid out.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_benchmark_befor_combnd IS 'BENEFIT BENCHMARK BEFORE COMBINED is BENCHMARK MARGIN LESS PROGRAM YEAR MARGIN multiplied by BENCHMARK MARGIN PAYOUT LEVEL. This is the value before Combined Farm % has been applied (combined farms only).'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.combined_benefit_percent IS 'COMBINED BENEFIT PERCENT is the percentage of the combined farm''s benefit that will be paid to this PIN/client (combined farms only).'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_benchmark IS 'BENEFIT BENCHMARK is the industry benchmark that will be compared with the calculated benefit. If this is a combined farm then the Combined Farm % has been applied.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_variance IS 'BENEFIT RISK VARIANCE is the percent difference between TOTAL BENEFIT and the BENEFIT BENCHMARK.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_variance_limit IS 'BENEFIT RISK VARIANCE LIMIT is the configured limit for the percentage difference between the TOTAL BENEFIT and the BENEFIT BENCHMARK. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_pass_ind IS 'MARGIN PASS INDICATOR denotes whether the Margin Test passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adjusted_reference_margin IS 'ADJUSTED REFERENCE MARGIN is the reference margin after accrual adjustments have been applied.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adj_reference_margin_variance IS 'ADJUSTED REFERENCE MARGIN VARIANCE is the percent difference between ADJUSTED REFERENCE MARGIN and the PROGRAM YEAR MARGIN.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adj_reference_margin_vrinc_lmt IS 'ADJUSTED REFERENCE MARGIN VARIANCE LIMIT is the configured limit for the percentage difference difference between ADJUSTED REFERENCE MARGIN and the PROGRAM YEAR MARGIN. If the margins are outside this limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_lmt_prct_ref_margin_ind IS 'WITHIN LIMIT CONFIGURED REFERENCE MARGIN INDICATOR indicates whether the program year margin was within the configured limit of the reference year margins.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_average IS 'MARGIN INDUSTRY AVERAGE is calculated by multiplying the average of the reference year BPUs for each commodity produced.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_avg_variance IS 'MARGIN INDUSTRY AVERAGE VARIANCE is calculated by subtracting the MARGIN INDUSTRY AVERAGE VARIANCE from the PROGRAM YEAR MARGIN and dividing that by the PROGRAM YEAR MARGIN.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_average_limit IS 'MARGIN INDUSTRY AVERAGE LIMIT is the configured limit for the MARGIN INDUSTRY AVERAGE VARIANCE. If it is outside the limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_wthn_lmt_indtry_avg_ind IS 'MARGIN WITHIN LIMIT INDUSTRY AVERAGE INDICATOR indicates if the MARGIN INDUSTRY AVERAGE VARIANCE was within the MARGIN INDUSTRY AVERAGE LIMIT.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.structural_change_pass_ind IS 'STRUCTURAL CHANGE PASS INDICATOR denotes whether the Structural Change Test passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_marg_accr_adjs IS 'PRODUCTION MARGIN ACCRUAL ADJUSTMENTS identifies the production margin associated with accrual adjustments.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_adj_reference_margin IS 'RATIO ADJUSTMENT REFERENCE MARGIN is the REFERENCE MARGIN using the Ratio structural change method.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_adj_reference_margin IS 'ADDITIVE ADJUSTMENT REFERENCE MARGIN is the REFERENCE MARGIN using the Additive structural change method.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_additive_ref_marg_diff IS 'RATIO ADDITIVE REFERENCE MARGIN DIFFERENCE is calculated by subtracting RATIO ADJUSTMENT REFERENCE MARGIN from ADDITIVE ADJUSTMENT REFERENCE MARGIN.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_add_diff_dollar_limit IS 'RATIO ADDITIVE DIFFERENCE DOLLAR LIMIT is the configured limit for the RATIO ADDITIVE REF MARGIN DIFFERENCE. If it is outside the limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_dlr_limit_ratio_add_ind IS 'WITHIN DOLLAR LIMIT RATIO ADDITIVE INDICATOR indicates if the RATIO ADDITIVE REFERENCE MARGIN DIFFERENCE was within the RATIO ADDITIVE DIFFERENCE DOLLAR LIMIT.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_division_ratio IS 'ADDITIVE DIVISION RATIO is calculated by dividing RATIO ADJUSTMENT REFERENCE MARGIN by ADDITIVE ADJUSTMENT REFERENCE MARGIN.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_division_ratio_limit IS 'ADDITIVE DIVISION RATIO LIMIT is the configured limit for the ADDITIVE DIVISION RATIO. If it is outside the limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_limit_addtiv_divisn_ind IS 'WITHIN LIMIT ADDITIVE DIVISION INDICATOR indicates if the ADDITIVE DIVISION RATIO was within the ADDITIVE DIVISION RATIO LIMIT.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.structual_change_method_to_use IS 'STRUCTUAL CHANGE METHOD TO USE is the Structural Change method that the test has determined should be used. If WITHIN DOLLAR LIMIT RATIO ADDITIVE INDICATOR and WITHIN LIMIT ADDITIVE DIVISION INDICATOR are Y then Ratio, if both are N then Additive, otherwise Manual selection.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.farm_size_ratio_limit IS 'FARM SIZE RATIO LIMIT is the configured limit for the farm size ratios for each reference year. If any of those are outside the limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.farm_size_rtios_within_lmt_ind IS 'FARM SIZE RATIOS WITHIN LIMIT INDICATOR indicates if the farm size ratios for each reference year were within the FARM SIZE RATIO LIMIT.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_risk_pass_ind IS 'REVENUE RISK PASS INDICATOR denotes whether Revenue Risk passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_risk_fruit_vg_pass_ind IS 'REVENUE RISK FRUIT VEGETABLES PASS INDICATOR denotes whether Revenue Risk - Fruits and Vegetables passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_rsk_grain_frg_pass_ind IS 'REVENUE RISK GRAIN FORAGE PASS INDICATOR denotes whether Revenue Risk - Grains, Forage, and Forage Seed passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_rsk_grain_forage_limit IS 'REVENUE RISK GRAIN FORAGE LIMIT is the configured limit for the REVENUE VARIANCE LIMIT in RSN REV G F FS INCM RSLT. If any income codes (LINE ITEMs) have a variance outside this limit, the Revenue Risk - Grains, Forage, and Forage Seed fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_indtry_av_cmp_pass_ind IS 'EXPENSE INDUSTRY AVERAGE is calculated by multiplying the average of the reference year BPU Expense Margins for each commodity produced.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_accr_adjs_indtry_av_cm IS 'EXPENSE ACCRUAL ADJUSTMENTS INDUSTRY AVERAGE COMPARISON identifies the expenses adjusted by accruals for the program year. For Expense Test - Industry Average Comparison, expense amounts for these codes are subtracted: 9661 - Containers and twine and 9836 - Commissions and Levies'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_average IS 'EXPENSE INDUSTRY AVERAGE is calculated by multiplying the average of the reference year BPU Expense Margins for each commodity produced.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_avg_variance IS 'EXPENSE INDUSTRY AVERAGE VARIANCE is calculated by subtracting the EXPENSE INDUSTRY AVERAGE from the EXPENSE ACCRUAL ADJUSTMENTS and dividing result by the EXPENSE ACCRUAL ADJUSTMENTS.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_average_limit IS 'EXPENSE INDUSTRY AVERAGE LIMIT is the configured limit for the EXPENSE INDUSTRY AVERAGE VARIANCE. If it is outside the limit, the test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_ref_year_comp_pass_ind IS 'EXPENSE REFERENCE YEAR COMPARISON PASS INDICATOR denotes whether the Expense Test - Reference Year Comparison passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_accr_adjs IS 'EXPENSE ACCRUAL ADJUSTMENTS identifies the expenses adjusted by accruals for the program year. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_1 IS 'EXPENSES AFTER STRUCTURE CHANGES YEAR MINUS 1 is the expenses after structure changes for the reference year that is program year minus 1.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_2 IS 'EXPENSES AFTER STRUCTURE CHANGES YEAR MINUS 2 is the expenses after structure changes for the reference year that is program year minus 2.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_3 IS 'EXPENSES AFTER STRUCTURE CHANGES YEAR MINUS 3 is the expenses after structure changes for the reference year that is program year minus 3.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_4 IS 'EXPENSES AFTER STRUCTURE CHANGES YEAR MINUS 4 is the expenses after structure changes for the reference year that is program year minus 4.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_5 IS 'EXPENSES AFTER STRUCTURE CHANGES YEAR MINUS 5 is the expenses after structure changes for the reference year that is program year minus 5.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_ref_yr_avg IS 'EXPENSES AFTER STRUCTURE CHANGES REFERENCE YEAR AVERAGE is the average expenses after structure changes of all the reference years.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_variance IS 'EXPENSES AFTER STRUCTURE CHANGES VARIANCE is the percent difference between EXPENSE ACCRUAL ADJUSTMENTS and EXPENSES AFTER STRUCTURE CHANGES REFERENCE YEAR AVERAGE.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_varian_lmt IS 'EXPENSES AFTER STRUCTURE CHANGES VARIANCE LIMIT is the percent difference between EXPENSE ACCRUAL ADJUSTMENTS and EXPENSES AFTER STRUCTURE CHANGES REFERENCE YEAR AVERAGE.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_pass_ind IS 'PRODUCTION PASS INDICATOR denotes whether Production Test passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_forage_fs_pass_ind IS 'PRODUCTION FORAGE SEED PASS INDICATOR denotes whether Production Test - Forage and Forage Seed passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_forage_qty_produced_limit IS 'PROD GRAIN QUANTITY PRODUCED LIMIT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN GRAIN RESULT. If it is outside the limit, the Grains Production test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_grains_pass_ind IS 'PRODUCTION GRAINS PASS INDICATOR denotes whether Production Test - Grains passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_grain_qty_produced_limit IS 'PRODUCTION GRAIN QUANTITY PRODUCED LIMIT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN GRAIN RESULT. If it is outside the limit, the Grains Production test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_fruit_veg_pass_ind IS 'PRODUCTION FRUIT VEGETABLES PASS IND denotes whether Production Test - Perishable Crops passed.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_fruit_veg_qty_prodcd_limt IS 'PROD FRUIT VEGETABLES QUANTITY PRODUCED LIMIT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN FRUT VEG RSLT. If it is outside the limit, the Perishables Production test fails.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_reasonabilty_test_results IS 'REASONABILITY TEST RESULT contains the results of reasonability tests run against the scenario. Reasonability tests are a set of automated tests that will flag a benefit if it falls outside expected boundaries.'
;


CREATE UNIQUE INDEX uk_rtr_asi ON farms.farm_reasonabilty_test_results(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_reasonabilty_test_results ADD 
    CONSTRAINT pk_rtr PRIMARY KEY (reasonability_test_result_id) USING INDEX TABLESPACE pg_default 
;
