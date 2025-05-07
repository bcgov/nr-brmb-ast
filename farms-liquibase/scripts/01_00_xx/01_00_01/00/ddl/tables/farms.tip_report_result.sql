CREATE TABLE tip_report_result(
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
    farm_type_3_threshold_met_indicator                  varchar(1),
    farm_type_2_threshold_met_indicator                  varchar(1),
    farm_type_1_threshold_met_indicator                  varchar(1),
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
    allowable_repayment_program_benefits                 numeric(16, 2)    NOT NULL,
    non_allowable_repay_program_benefits                 numeric(16, 2)    NOT NULL,
    commodity_purchases_repay_benefits                   numeric(16, 2)    NOT NULL,
    total_income_benchmark                               numeric(16, 2),
    allowable_income_per_100                             numeric(16, 2)    NOT NULL,
    allowable_income_per_100_5_year                      numeric(16, 2),
    allowable_income_per_100_benchmark                   numeric(16, 2),
    other_farm_income_per_100                            numeric(16, 2)    NOT NULL,
    other_farm_income_per_100_5_year                     numeric(16, 2),
    other_farm_income_per_100_benchmark                  numeric(16, 2),
    allowable_expenses_per_100                           numeric(16, 2)    NOT NULL,
    allowable_expenses_per_100_5_year                    numeric(16, 2),
    allowable_expenses_per_100_benchmark                 numeric(16, 2),
    non_allowable_expenses_per_100                       numeric(16, 2),
    non_allowable_expenses_per_100_5_year                numeric(16, 2),
    non_allowable_expenses_per_100_benchmark             numeric(16, 2),
    production_margin_per_100                            numeric(16, 2)    NOT NULL,
    production_margin_per_100_5_year                     numeric(16, 2),
    production_margin_per_100_benchmark                  numeric(16, 2),
    total_expenses_per_100                               numeric(16, 2)    NOT NULL,
    total_expenses_per_100_5_year                        numeric(16, 2),
    total_expenses_per_100_benchmark                     numeric(16, 2),
    commodity_purchases_repay_benefit_100                numeric(16, 2)    NOT NULL,
    commodity_purchases_repay_benefit_100_5_year         numeric(16, 2),
    commodity_purchases_repay_benefit_100_benchmark      numeric(16, 2),
    commodity_purchases_per_100_high_25_percentile       numeric(16, 2),
    non_allowble_repay_program_benefit_100               numeric(16, 2)    NOT NULL,
    non_allowable_repay_program_benefit_100_5_year       numeric(16, 2),
    non_allowable_repay_program_benefit_100_benchmark    numeric(16, 2),
    non_allowable_repay_program_benefit_100_h25p         numeric(16, 2),
    production_margin                                    numeric(16, 2)    NOT NULL,
    production_margin_ratio                              numeric(18, 4)    NOT NULL,
    production_margin_ratio_5_year                       numeric(18, 4),
    production_margin_ratio_benchmark                    numeric(18, 4),
    production_margin_ratio_low_25_percentile            numeric(16, 2),
    net_margin                                           numeric(16, 2)    NOT NULL,
    net_margin_per_100                                   numeric(16, 2)    NOT NULL,
    net_margin_per_100_5_year                            numeric(16, 2),
    net_margin_per_100_benchmark                         numeric(16, 2),
    operating_cost                                       numeric(16, 2)    NOT NULL,
    operating_cost_ratio                                 numeric(18, 4)    NOT NULL,
    operating_cost_ratio_5_year                          numeric(18, 4),
    operating_cost_ratio_benchmark                       numeric(18, 4),
    operating_cost_ratio_high_25_percentile              numeric(16, 2),
    direct_expenses                                      numeric(16, 2)    NOT NULL,
    direct_expenses_ratio                                numeric(18, 4)    NOT NULL,
    direct_expenses_ratio_5_year                         numeric(18, 4),
    direct_expenses_ratio_benchmark                      numeric(18, 4),
    direct_expenses_ratio_high_25_percentile             numeric(16, 2),
    machinery_expenses                                   numeric(16, 2)    NOT NULL,
    machinery_expenses_ratio                             numeric(18, 4)    NOT NULL,
    machinery_expenses_ratio_5_year                      numeric(18, 4),
    machinery_expenses_ratio_benchmark                   numeric(18, 4),
    machinery_expenses_ratio_high_25_percentile          numeric(16, 2),
    land_build_expenses                                  numeric(16, 2)    NOT NULL,
    land_build_expenses_ratio                            numeric(18, 4)    NOT NULL,
    land_build_expens_ratio_5_year                       numeric(18, 4),
    land_build_expenses_ratio_benchmark                  numeric(18, 4),
    land_build_expenses_ratio_high_25_percentile         numeric(16, 2),
    overhead_expenses                                    numeric(16, 2)    NOT NULL,
    overhead_expenses_ratio                              numeric(18, 4)    NOT NULL,
    overhead_expenses_ratio_5_year                       numeric(18, 4),
    overhead_expenses_ratio_benchmark                    numeric(18, 4),
    overhead_expenses_ratio_high_25_percentile           numeric(16, 2),
    use_for_benchmarks_indicator                         varchar(1)        NOT NULL,
    reference_year_indicator                             varchar(1)        NOT NULL,
    generated_date                                       date              NOT NULL,
    commodity_purchases_repay_100_rating                 varchar(10),
    production_margin_ratio_rating                       varchar(10),
    operating_cost_ratio_rating                          varchar(10),
    direct_expenses_ratio_rating                         varchar(10),
    machinery_expenses_ratio_rating                      varchar(10),
    land_build_expenses_ratio_rating                     varchar(10),
    overhead_expenses_ratio_rating                       varchar(10),
    program_year_id                                      numeric(10, 0)    NOT NULL,
    farming_operation_id                                 numeric(10, 0)    NOT NULL,
    parent_tip_report_result_id                          numeric(10, 0),
    tip_benchmark_year_id                                numeric(10, 0),
    tip_benchmark_year_id_type_3                         numeric(10, 0),
    tip_benchmark_year_id_type_2                         numeric(10, 0),
    tip_benchmark_year_id_type_1                         numeric(10, 0),
    revision_count                                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                          varchar(30)       NOT NULL,
    create_date                                          timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                          varchar(30),
    update_date                                          timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_report_result.tip_report_result_id IS 'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.'
;
COMMENT ON COLUMN tip_report_result.participant_pin IS 'PARTICIPANT PIN (Personal Information Number) is the unique AgriStability/AgriInvest pin for this producer. Was previously CAIS PIN and NISA PIN.'
;
COMMENT ON COLUMN tip_report_result.year IS 'YEAR is the program year or reference year this data pertains to.'
;
COMMENT ON COLUMN tip_report_result.alignment_key IS 'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.'
;
COMMENT ON COLUMN tip_report_result.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given year. Operations may have different OPERATION NUMBER in different years.'
;
COMMENT ON COLUMN tip_report_result.partnership_pin IS 'PARTNERSHIP PIN is a unique identifier for a farming operation partnership.'
;
COMMENT ON COLUMN tip_report_result.partnership_name IS 'PARTNERSHIP NAME is the name of the partnership for the FARMING OPERATION.'
;
COMMENT ON COLUMN tip_report_result.partnership_percent IS 'PARTNERSHIP PERCENT is the Percentage of the  Partnership (100% will be stored as 1.0).'
;
COMMENT ON COLUMN tip_report_result.farm_type_3_name IS 'FARM TYPE 3 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN tip_report_result.farm_type_2_name IS 'FARM TYPE 2 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN tip_report_result.farm_type_1_name IS 'FARM TYPE 1 NAME is the unique business name for the type of farm.'
;
COMMENT ON COLUMN tip_report_result.farm_type_3_percent IS 'FARM TYPE 3 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN tip_report_result.farm_type_2_percent IS 'FARM TYPE 2 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN tip_report_result.farm_type_1_percent IS 'FARM TYPE 1 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.'
;
COMMENT ON COLUMN tip_report_result.farm_type_3_threshold_met_indicator IS 'FARM TYPE 3 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN tip_report_result.farm_type_2_threshold_met_indicator IS 'FARM TYPE 2 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN tip_report_result.farm_type_1_threshold_met_indicator IS 'FARM TYPE 1 THRESHOLD MET INDICATOR indicates whether the farming operation meets the threshold required to be classified as this farm type.'
;
COMMENT ON COLUMN tip_report_result.farm_type_level IS 'FARM TYPE LEVEL is the level of farm type that will be used to generate bencharks and compare with other farms. Possible values 1, 2, or 3.'
;
COMMENT ON COLUMN tip_report_result.income_range_low IS 'INCOME RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.income_range_high IS 'INCOME RANGE HIGH is the maximum range for a grouping for a TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.group_farming_operation_count IS 'GROUP FARMING OPERATION COUNT is the number of operations in the FARM TYPE LEVEL with income between INCOME RANGE LOW and INCOME RANGE HIGH.'
;
COMMENT ON COLUMN tip_report_result.reference_year_count IS 'REFERENCE YEAR COUNT is the number of years available to compute averages within the past five years (YEAR - 5 to YEAR - 1).'
;
COMMENT ON COLUMN tip_report_result.total_income IS 'TOTAL INCOME is the total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.total_expenses IS 'TOTAL EXPENSES is the total amount of allowable and non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN tip_report_result.allowable_income IS 'ALLOWABLE INCOME is the total amount of allowable income of the farming operation.'
;
COMMENT ON COLUMN tip_report_result.allowable_expenses IS 'ALLOWABLE EXPENSES is the total amount of allowable income of the farming operation.'
;
COMMENT ON COLUMN tip_report_result.non_allowable_income IS 'NON ALLOWABLE INCOME is the total amount of non-allowable income of the farming operation.'
;
COMMENT ON COLUMN tip_report_result.non_allowable_expenses IS 'NON ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN tip_report_result.commodity_income IS 'COMMODITY INCOME is the total income from commodity sales. All commodities have an entry in TIP LINE ITEMS where TIP FARM TYPE 1 LOOKUP ID is not null.'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases IS 'COMMODITY PURCHASES is the total expenses for commodity purchases. All commodities have an entry in TIP LINE ITEMS where TIP FARM TYPE 1 LOOKUP ID is not null.'
;
COMMENT ON COLUMN tip_report_result.allowable_repayment_program_benefits IS 'ALLOWABLE REPAYMENT PROGRAM BENEFITS is the total allowable expenses for repayment of AgriStability benefits.'
;
COMMENT ON COLUMN tip_report_result.non_allowable_repay_program_benefits IS 'NON ALLOWABLE REPAY PROGRAM BENEFITS is the total non-allowable expenses for repayment of AgriStability benefits.'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_repay_benefits IS 'COMMODITY PURCHASES REPAY BENEFITS is the total allowable expenses for for commodity purchases and repayment of AgriStability benefits.'
;
COMMENT ON COLUMN tip_report_result.total_income_benchmark IS 'TOTAL INCOME BENCHMARK is the industry benchmark total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.allowable_income_per_100 IS 'ALLOWABLE INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.allowable_income_per_100_5_year IS 'ALLOWABLE INCOME PER 100 5 YEAR is the 5-year average total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.allowable_income_per_100_benchmark IS 'ALLOWABLE INCOME PER 100 BENCHMARK is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is an average of farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.other_farm_income_per_100 IS 'OTHER FARM INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.other_farm_income_per_100_5_year IS 'OTHER FARM INCOME PER 100 5 YEAR is the 5-year average total amount of non-allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.other_farm_income_per_100_benchmark IS 'OTHER FARM INCOME PER 100 BENCHMARK is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.allowable_expenses_per_100 IS 'ALLOWABLE EXPENSES PER 100 is the total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.allowable_expenses_per_100_5_year IS 'ALLOWABLE EXPENSES PER 100 5 YEAR is the 5-year average total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.allowable_expenses_per_100_benchmark IS 'ALLOWABLE EXPENSES PER 100 BENCHMARK is the industry benchmark average total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_expenses_per_100 IS 'NON ALLOWABLE EXPENSES PER 100 is the amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_expenses_per_100_5_year IS 'NON ALLOWABLE EXPENSES PER 100 5 YEAR is the 5-year average amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_expenses_per_100_benchmark IS 'NON ALLOWABLE EXPENSES PER 100 BENCHMARK is the industry benchmark amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.production_margin_per_100 IS 'PRODUCTION MARGIN PER 100 is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI) and multiplied by 100.'
;
COMMENT ON COLUMN tip_report_result.production_margin_per_100_5_year IS 'PRODUCTION MARGIN PER 100 5 YEAR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN tip_report_result.production_margin_per_100_benchmark IS 'PRODUCTION MARGIN PER 100 BENCHMARK is the production_margin_per_100_bch of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN tip_report_result.total_expenses_per_100 IS 'TOTAL EXPENSES PER 100 is the total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.total_expenses_per_100_5_year IS 'TOTAL EXPENSES PER 100 5 YEAR is the 5-year average total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.total_expenses_per_100_benchmark IS 'TOTAL EXPENSES PER 100 BENCHMARK is the industry benchmark total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_repay_benefit_100 IS 'COMMODITY PURCHASES REPAY BENEFIT 100 is the amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_repay_benefit_100_5_year IS 'COMMODITY PURCHASES REPAY BENEFIT 100 5 YEAR is the 5-year average amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_repay_benefit_100_benchmark IS 'COMMODITY PURCHASES REPAY BENEFIT 100 BENCHMARK is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_per_100_high_25_percentile IS 'COMMODITY PURCHASES PER 100 HIGH 25 PERCENTILE is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowble_repay_program_benefit_100 IS 'NON ALLOWBLE REPAY PROGRAM BENEFIT 100 is the amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_repay_program_benefit_100_5_year IS 'NON ALLOWABLE REPAY PROGRAM BENEFIT 100 5 YEAR is the 5-year average amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_repay_program_benefit_100_benchmark IS 'COMMODITY PURCHASES REPAY BENEFIT 100 BENCHMARK is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.non_allowable_repay_program_benefit_100_h25p IS 'COMMODITY PURCHASES PER 100 HIGH 25 PERCENTILE is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_report_result.production_margin IS 'PRODUCTION MARGIN is the margin amount (ALLOWABLE INCOME minus ALLOWABLE EXPENSES).'
;
COMMENT ON COLUMN tip_report_result.production_margin_ratio IS 'PRODUCTION MARGIN RATIO is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.production_margin_ratio_5_year IS 'PRODUCTION MARGIN RATIO 5 YEAR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).'
;
COMMENT ON COLUMN tip_report_result.production_margin_ratio_benchmark IS 'PRODUCTION MARGIN RATIO BENCHMARK is the industry benchmark ratio of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.production_margin_ratio_low_25_percentile IS 'PRODUCTION MARGIN RATIO LOW 25 PERCENTILE is the higher 25th percentile of higher 25th percentile of PRODUCTION MARGIN RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.net_margin IS 'NET MARGIN is the margin amount (TOTAL INCOME minus TOTAL EXPENSES).'
;
COMMENT ON COLUMN tip_report_result.net_margin_per_100 IS 'NET MARGIN PER 100 is the NET MARGIN divided by the TOTAL INCOME (GFI) and multiplied by 100.'
;
COMMENT ON COLUMN tip_report_result.net_margin_per_100_5_year IS 'NET MARGIN PER 100 5 YEAR is the 5-year average of NET MARGIN PER 100.'
;
COMMENT ON COLUMN tip_report_result.net_margin_per_100_benchmark IS 'NET MARGIN PER 100 BENCHMARK is the median NET MARGIN PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.operating_cost IS 'OPERATING COST is ALLOWABLE EXPENSES minus amounts for depreciation expense codes.'
;
COMMENT ON COLUMN tip_report_result.operating_cost_ratio IS 'OPERATING COST RATIO is the OPERATING COST divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.operating_cost_ratio_5_year IS 'OPERATING COST RATIO 5 YEAR is the 5-year average OPERATING COST divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.operating_cost_ratio_benchmark IS 'OPERATING COST RATIO BENCHMARK is the industry benchmark ratio of the OPERATING COST divided by the TOTAL INCOME (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.operating_cost_ratio_high_25_percentile IS 'OPERATING COST RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OPERATING COST RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.direct_expenses IS 'DIRECT EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_report_result.direct_expenses_ratio IS 'DIRECT EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.direct_expenses_ratio_5_year IS 'DIRECT EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN tip_report_result.direct_expenses_ratio_benchmark IS 'DIRECT EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the DIRECT EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.direct_expenses_ratio_high_25_percentile IS 'DIRECT EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of DIRECT EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses IS 'MACHINERY EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses_ratio IS 'MACHINERY EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses_ratio_5_year IS 'MACHINERY EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses_ratio_benchmark IS 'MACHINERY EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the MACHINERY EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses_ratio_high_25_percentile IS 'MACHINERY EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of MACHINERY EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.land_build_expenses IS 'LAND BUILD is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_report_result.land_build_expenses_ratio IS 'LAND BUILD EXPENSES RATIO is the LAND BUILD divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.land_build_expens_ratio_5_year IS 'LAND BUILD EXPENSES RATIO 5 YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN tip_report_result.land_build_expenses_ratio_benchmark IS 'LAND BUILD EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the LAND BUILD RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.land_build_expenses_ratio_high_25_percentile IS 'LAND BUILD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of LAND BUILD RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses IS 'OVERHEAD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses_ratio IS 'OVERHEAD EXPENSES RATIO is the OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses_ratio_5_year IS 'OVERHEAD EXPENSES RATIO 5 YEAR is the 5-year average of (OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI)).'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses_ratio_benchmark IS 'OVERHEAD EXPENSES RATIO BENCHMARK is the industry benchmark ratio of the OVERHEAD EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses_ratio_high_25_percentile IS 'OVERHEAD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OVERHEAD EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_report_result.use_for_benchmarks_indicator IS 'USE FOR BENCHMARKS INDICATOR indicates that this record should be used when calculating benchmarks rather than the others with the same partnership PIN. Prevents counting the same operation/partnership multiple times.'
;
COMMENT ON COLUMN tip_report_result.reference_year_indicator IS 'REFERENCE YEAR INDICATOR indicates that this record is for a reference year (not the program year).'
;
COMMENT ON COLUMN tip_report_result.generated_date IS 'GENERATED DATE identifies the date and time that the TIP Report result was generated.'
;
COMMENT ON COLUMN tip_report_result.commodity_purchases_repay_100_rating IS 'COMMODITY PURCHASES REPAY 100 RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.'
;
COMMENT ON COLUMN tip_report_result.production_margin_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.operating_cost_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.direct_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.machinery_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.land_build_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.overhead_expenses_ratio_rating IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN tip_report_result.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN tip_report_result.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN tip_report_result.parent_tip_report_result_id IS 'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.'
;
COMMENT ON COLUMN tip_report_result.tip_benchmark_year_id IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.tip_benchmark_year_id_type_3 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.tip_benchmark_year_id_type_2 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.tip_benchmark_year_id_type_1 IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_report_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_report_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_report_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_report_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_report_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_report_result IS 'TIP REPORT RESULT contains the results of a TIP Report run against a FARMING OPERATION. The TIP Report compares income and expenses of an operation to an industry benchmark.'
;

