CREATE TABLE farms.farm_tip_report_results(
    tip_report_result_id                                 numeric(10, 0)    NOT NULL,
    participant_pin                                      numeric(9, 0)     NOT NULL,
    year                                                 numeric(4, 0)     NOT NULL,
    alignment_key                                        varchar(2)        NOT NULL,
    operation_number                                     numeric(4, 0)     NOT NULL,
    partnership_pin                                      numeric(9, 0)     NOT NULL,
    partnership_name                                     varchar(42),
    partnership_percent                                  numeric(7, 4)     NOT NULL,
    farm_type_3_name                                     varchar(256)      NOT NULL,
    farm_type_2_name                                     varchar(256)      NOT NULL,
    farm_type_1_name                                     varchar(256)      NOT NULL,
    farm_type_3_percent                                  numeric(4, 3),
    farm_type_2_percent                                  numeric(4, 3),
    farm_type_1_percent                                  numeric(4, 3),
    farm_type_3_threshold_met_ind                  varchar(1),
    farm_type_2_threshold_met_ind                  varchar(1),
    farm_type_1_threshold_met_ind                  varchar(1),
    farm_type_level                                      numeric(1, 0),
    income_range_low                                     numeric(13, 2),
    income_range_high                                    numeric(13, 2),
    group_farming_operation_count                        numeric(6, 0),
    reference_year_count                                 numeric(1, 0),
    total_income                                         numeric(16, 2)    NOT NULL,
    total_expenses                                       numeric(16, 2)    NOT NULL,
    allowable_income                                     numeric(16, 2)    NOT NULL,
    allowable_expenses                                   numeric(16, 2)    NOT NULL,
    non_allowable_income                                 numeric(16, 2)    NOT NULL,
    non_allowable_expenses                               numeric(16, 2)    NOT NULL,
    commodity_income                                     numeric(16, 2)    NOT NULL,
    commodity_purchases                                  numeric(16, 2)    NOT NULL,
    allowable_repaymnt_prgm_benfts                 numeric(16, 2)    NOT NULL,
    non_allowable_repay_prgm_bnfts                 numeric(16, 2)    NOT NULL,
    commdity_purchases_repay_bnfts                   numeric(16, 2)    NOT NULL,
    total_income_benchmark                               numeric(16, 2),
    allowable_income_per_100                             numeric(16, 2)    NOT NULL,
    allowable_income_per_100_5year                      numeric(16, 2),
    allowable_income_per_100_bench                   numeric(16, 2),
    other_farm_income_per_100                            numeric(16, 2)    NOT NULL,
    other_farm_income_per100_5year                     numeric(16, 2),
    other_farm_income_per100_bench                  numeric(16, 2),
    allowable_expenses_per_100                           numeric(16, 2)    NOT NULL,
    allowable_expens_per_100_5year                    numeric(16, 2),
    allowable_expens_per_100_bench                 numeric(16, 2),
    non_allowable_expenses_per_100                       numeric(16, 2),
    non_allowabl_expns_per_100_5yr                numeric(16, 2),
    non_allowabl_expns_per_100_bch             numeric(16, 2),
    production_margin_per_100                            numeric(16, 2)    NOT NULL,
    production_margin_per_100_5yr                     numeric(16, 2),
    production_margin_per_100_bch                  numeric(16, 2),
    total_expenses_per_100                               numeric(16, 2)    NOT NULL,
    total_expenses_per_100_5year                        numeric(16, 2),
    total_expenses_per_100_bench                     numeric(16, 2),
    cmmdty_prchs_rpay_bnft_100                numeric(16, 2)    NOT NULL,
    cmmdty_prchs_rpay_bnft_100_5yr         numeric(16, 2),
    cmmdty_prchs_rpay_bnft_100_bch      numeric(16, 2),
    cmmdty_prchs_per100_high_25pct       numeric(16, 2),
    non_allowbl_repay_pgm_bnft_100               numeric(16, 2)    NOT NULL,
    non_allw_rpay_pgm_bnft_100_5yr       numeric(16, 2),
    non_allw_rpay_pgm_bnft_100_bch    numeric(16, 2),
    non_allw_rpay_pgm_bnf_100_h25p         numeric(16, 2),
    production_margin                                    numeric(16, 2)    NOT NULL,
    production_margin_ratio                              numeric(18, 4)    NOT NULL,
    production_margin_ratio_5year                       numeric(18, 4),
    production_margin_ratio_bench                    numeric(18, 4),
    prodtn_margin_ratio_low_25pct            numeric(16, 2),
    net_margin                                           numeric(16, 2)    NOT NULL,
    net_margin_per_100                                   numeric(16, 2)    NOT NULL,
    net_margin_per_100_5yr                            numeric(16, 2),
    net_margin_per_100_bench                         numeric(16, 2),
    operating_cost                                       numeric(16, 2)    NOT NULL,
    operating_cost_ratio                                 numeric(18, 4)    NOT NULL,
    operating_cost_ratio_5year                          numeric(18, 4),
    operating_cost_ratio_bench                       numeric(18, 4),
    operatng_cost_ratio_high_25pct              numeric(16, 2),
    direct_expenses                                      numeric(16, 2)    NOT NULL,
    direct_expenses_ratio                                numeric(18, 4)    NOT NULL,
    direct_expenses_ratio_5year                         numeric(18, 4),
    direct_expenses_ratio_bench                      numeric(18, 4),
    direct_expens_ratio_high_25pct             numeric(16, 2),
    machinery_expenses                                   numeric(16, 2)    NOT NULL,
    machinery_expenses_ratio                             numeric(18, 4)    NOT NULL,
    machinery_expenses_ratio_5year                      numeric(18, 4),
    machinery_expenses_ratio_bench                   numeric(18, 4),
    machnry_expns_ratio_high_25pct          numeric(16, 2),
    land_build_expenses                                  numeric(16, 2)    NOT NULL,
    land_build_expenses_ratio                            numeric(18, 4)    NOT NULL,
    land_build_expens_ratio_5_year                       numeric(18, 4),
    land_build_expens_ratio_bench                  numeric(18, 4),
    land_bld_expn_ratio_high_25pct         numeric(16, 2),
    overhead_expenses                                    numeric(16, 2)    NOT NULL,
    overhead_expenses_ratio                              numeric(18, 4)    NOT NULL,
    overhead_expenses_ratio_5year                       numeric(18, 4),
    overhead_expenses_ratio_bench                    numeric(18, 4),
    overhead_expn_ratio_high_25pct           numeric(16, 2),
    use_for_benchmarks_ind                         varchar(1)        NOT NULL,
    reference_year_ind                             varchar(1)        NOT NULL,
    generated_date                                       date              NOT NULL,
    cmmdty_prchs_rpay_b_100_rating                 varchar(10),
    production_margin_ratio_rating                       varchar(10),
    operating_cost_ratio_rating                          varchar(10),
    direct_expenses_ratio_rating                         varchar(10),
    machinery_expenses_ratio_ratng                      varchar(10),
    land_build_expens_ratio_rating                     varchar(10),
    overhead_expenses_ratio_rating                       varchar(10),
    program_year_id                                      numeric(10, 0)    NOT NULL,
    farming_operation_id                                 numeric(10, 0)    NOT NULL,
    parent_tip_report_result_id                          numeric(10, 0),
    tip_benchmark_year_id                                numeric(10, 0),
    tip_benchmark_year_id_type_3                         numeric(10, 0),
    tip_benchmark_year_id_type_2                         numeric(10, 0),
    tip_benchmark_year_id_type_1                         numeric(10, 0),
    revision_count                                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                          varchar(30)       NOT NULL,
    when_created                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                          varchar(30),
    when_updated                                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_tip_report_results.tip_report_result_id IS 'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.participant_pin IS 'PARTICIPANT PIN (Personal Information Number) is the unique AgriStability/AgriInvest pin for this producer. Was previously CAIS PIN and NISA PIN.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.year IS 'YEAR is the program year or reference year this data pertains to.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.alignment_key IS 'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given year. Operations may have different OPERATION NUMBER in different years.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_pin IS 'PARTNERSHIP PIN is a unique identifier for a farming operation partnership.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_name IS 'PARTNERSHIP NAME is the name of the partnership for the FARMING OPERATION.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_percent IS 'PARTNERSHIP PERCENT is the Percentage of the  Partnership (100% will be stored as 1.0).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_name IS 'FARM TYPE 3 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_name IS 'FARM TYPE 2 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_name IS 'FARM TYPE 1 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_percent IS 'FARM TYPE 3 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_percent IS 'FARM TYPE 2 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_percent IS 'FARM TYPE 1 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_threshold_met_ind IS 'FARM TYPE 3 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_threshold_met_ind IS 'FARM TYPE 2 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_threshold_met_ind IS 'FARM TYPE 1 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_level IS 'FARM TYPE LEVEL is the level of farm type that will be used to generate bencharks and compare with other farms. Possible values 1, 2, or 3.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.income_range_low IS 'INCOME RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.income_range_high IS 'INCOME RANGE HIGH is the maximum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.group_farming_operation_count IS 'GROUP FARMING OPERATION COUNT is the number of operations in the FARM TYPE LEVEL with income between INCOME RANGE LOW and INCOME RANGE HIGH.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.reference_year_count IS 'REFERENCE YEAR COUNT is the number of years available to compute averages within the past five years (YEAR - 5 to YEAR - 1).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_income IS 'TOTAL INCOME is the total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses IS 'TOTAL EXPENSES is the total amount of allowable and non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income IS 'ALLOWABLE INCOME is the total amount of allowable income of the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expenses IS 'ALLOWABLE EXPENSES is the total amount of allowable income of the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_income IS 'NON ALLOWABLE INCOME is the total amount of non-allowable income of the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_expenses IS 'NON ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.commodity_income IS 'COMMODITY INCOME is the total income from commodity sales. All commodities have an entry in TIP LINE ITEMS where TIP FARM TYPE 1 LOOKUP ID is not null.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.commodity_purchases IS 'COMMODITY PURCHASES is the total expenses for commodity purchases. All commodities have an entry in TIP LINE ITEMS where TIP FARM TYPE 1 LOOKUP ID is not null.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_repaymnt_prgm_benfts IS 'ALLOWABLE REPAYMENT PROGRAM BENEFITS is the total allowable expenses for repayment of AgriStability benefits.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_repay_prgm_bnfts IS 'NON ALLOWABLE REPAY PROGRAM BENEFITS is the total non-allowable expenses for repayment of AgriStability benefits.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.commdity_purchases_repay_bnfts IS 'COMMODITY PURCHASES REPAY BENEFITS is the total allowable expenses for for commodity purchases and repayment of AgriStability benefits.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_income_benchmark IS 'TOTAL INCOME BENCHMARK is the industry benchmark total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100 IS 'ALLOWABLE INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100_5year IS 'ALLOWABLE INCOME PER 100 5 YEAR is the 5-year average total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100_bench IS 'ALLOWABLE INCOME PER 100 BENCHMARK is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is an average of farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per_100 IS 'OTHER FARM INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per100_5year IS 'OTHER FARM INCOME PER 100 5 YEAR is the 5-year average total amount of non-allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per100_bench IS 'OTHER FARM INCOME PER 100 BENCHMARK is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expenses_per_100 IS 'ALLOWABLE EXPENSES PER 100 is the total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expens_per_100_5year IS 'ALLOWABLE EXPENSES PER 100 5 YEAR is the 5-year average total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expens_per_100_bench IS 'ALLOWABLE EXPENSES PER 100 BENCHMARK is the industry benchmark average total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_expenses_per_100 IS 'NON ALLOWABLE EXPENSES PER 100 is the amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowabl_expns_per_100_5yr IS 'NON ALLOWABLE EXPENSES PER 100 5 YEAR is the 5-year average amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowabl_expns_per_100_bch IS 'NON ALLOWABLE EXPENSES PER 100 BENCHMARK is the industry benchmark amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100 IS 'PRODUCTION MARGIN PER 100 is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI) and multiplied by 100.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100_5yr IS 'PRODUCTION MARGIN PER 100 5 YEAR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100_bch IS 'PRODUCTION MARGIN PER 100 BENCHMARK is the production_margin_per_100_bch of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100 IS 'TOTAL EXPENSES PER 100 is the total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100_5year IS 'TOTAL EXPENSES PER 100 5 YEAR is the 5-year average total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100_bench IS 'TOTAL EXPENSES PER 100 BENCHMARK is the industry benchmark total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100 IS 'COMMODITY PURCHASES REPAY BENEFIT 100 is the amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100_5yr IS 'COMMODITY PURCHASES REPAY BENEFIT 100 5 YEAR is the 5-year average amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100_bch IS 'COMMODITY PURCHASES REPAY BENEFIT 100 BENCHMARK is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_per100_high_25pct IS 'COMMODITY PURCHASES PER 100 HIGH 25 PERCENTILE is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowbl_repay_pgm_bnft_100 IS 'NON ALLOWBLE REPAY PROGRAM BENEFIT 100 is the amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnft_100_5yr IS 'NON ALLOWABLE REPAY PROGRAM BENEFIT 100 5 YEAR is the 5-year average amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnft_100_bch IS 'COMMODITY PURCHASES REPAY BENEFIT 100 BENCHMARK is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnf_100_h25p IS 'COMMODITY PURCHASES PER 100 HIGH 25 PERCENTILE is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin IS 'PRODUCTION MARGIN is the margin amount (ALLOWABLE INCOME minus ALLOWABLE EXPENSES).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio IS 'PRODUCTION MARGIN RATIO is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_5year IS 'PRODUCTION MARGIN RATIO 5 YEAR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_bench IS 'PRODUCTION MARGIN RATIO BENCHMARK is the industry benchmark ratio of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.prodtn_margin_ratio_low_25pct IS 'PRODUCTION MARGIN RATIO LOW 25 PERCENTILE is the higher 25th percentile of higher 25th percentile of PRODUCTION MARGIN RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin IS 'NET MARGIN is the margin amount (TOTAL INCOME minus TOTAL EXPENSES).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100 IS 'NET MARGIN PER 100 is the NET MARGIN divided by the TOTAL INCOME (GFI) and multiplied by 100.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100_5yr IS 'NET MARGIN PER 100 5 YEAR is the 5-year average of NET MARGIN PER 100.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100_bench IS 'NET MARGIN PER 100 BENCHMARK is the median NET MARGIN PER 100 for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost IS 'OPERATING COST is ALLOWABLE EXPENSES minus amounts for depreciation expense codes.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio IS 'OPERATING COST RATIO is the OPERATING COST divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_5year IS 'OPERATING COST RATIO 5 YEAR is the 5-year average OPERATING COST divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_bench IS 'OPERATING COST RATIO BENCHMARK is the industry benchmark ratio of the OPERATING COST divided by the TOTAL INCOME (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operatng_cost_ratio_high_25pct IS 'OPERATING COST RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OPERATING COST RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses IS 'DIRECT EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio IS 'DIRECT EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_5year IS 'DIRECT EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_bench IS 'DIRECT EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the DIRECT EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expens_ratio_high_25pct IS 'DIRECT EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of DIRECT EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses IS 'MACHINERY EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio IS 'MACHINERY EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_5year IS 'MACHINERY EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_bench IS 'MACHINERY EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the MACHINERY EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machnry_expns_ratio_high_25pct IS 'MACHINERY EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of MACHINERY EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expenses IS 'LAND BUILD is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expenses_ratio IS 'LAND BUILD EXPENSES RATIO is the LAND BUILD divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_5_year IS 'LAND BUILD EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_bench IS 'LAND BUILD EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the LAND BUILD RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_bld_expn_ratio_high_25pct IS 'LAND BUILD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of LAND BUILD RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses IS 'OVERHEAD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio IS 'OVERHEAD EXPENSES RATIO is the OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_5year IS 'OVERHEAD EXPENSES RATIO 5 YEAR is the 5-year average of (OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_bench IS 'OVERHEAD EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the OVERHEAD EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expn_ratio_high_25pct IS 'OVERHEAD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OVERHEAD EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.use_for_benchmarks_ind IS 'USE FOR BENCHMARKS INDICATOR indicates that this record should be used when calculating benchmarks rather than the others with the same partnership PIN. Prevents counting the same operation/partnership multiple times.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.reference_year_ind IS 'REFERENCE YEAR INDICATOR indicates that this record is for a reference year (not the program year).'
;
COMMENT ON COLUMN farms.farm_tip_report_results.generated_date IS 'GENERATED DATE identifies the date and time that the TIP Report result was generated.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_b_100_rating IS 'COMMODITY PURCHASES REPAY 100 RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_ratng IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_report_results.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.parent_tip_report_result_id IS 'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_3 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_2 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_1 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_report_results.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_tip_report_results IS 'TIP REPORT RESULT contains the results of a TIP Report run against a FARMING OPERATION. The TIP Report compares income and expenses of an operation to an industry benchmark.'
;


CREATE INDEX ix_trr_cpr1r ON farms.farm_tip_report_results(cmmdty_prchs_rpay_b_100_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_derr ON farms.farm_tip_report_results(direct_expenses_ratio_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_lberr ON farms.farm_tip_report_results(land_build_expens_ratio_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_merr ON farms.farm_tip_report_results(machinery_expenses_ratio_ratng)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_ocrr ON farms.farm_tip_report_results(operating_cost_ratio_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_oerr ON farms.farm_tip_report_results(overhead_expenses_ratio_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_pmrr ON farms.farm_tip_report_results(production_margin_ratio_rating)
 TABLESPACE pg_default
;
CREATE INDEX ix_trr_ptrri ON farms.farm_tip_report_results(parent_tip_report_result_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_trr_foi_ptrri ON farms.farm_tip_report_results(farming_operation_id, parent_tip_report_result_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_trr_pyi_ak_ptrri ON farms.farm_tip_report_results(program_year_id, alignment_key, parent_tip_report_result_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_tip_report_results ADD 
    CONSTRAINT pk_trr PRIMARY KEY (tip_report_result_id) USING INDEX TABLESPACE pg_default 
;
