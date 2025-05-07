CREATE TABLE tip_benchmark_year(
    tip_benchmark_year_id                                 numeric(10, 0)    NOT NULL,
    program_year                                          numeric(4, 0)     NOT NULL,
    reference_year                                        numeric(4, 0)     NOT NULL,
    farm_type_3_name                                      varchar(256),
    farm_type_2_name                                      varchar(256),
    farm_type_1_name                                      varchar(256),
    income_range_low                                      numeric(12, 2)    NOT NULL,
    income_range_high                                     numeric(12, 2)    NOT NULL,
    minimum_group_count                                   numeric(3, 0)     NOT NULL,
    farming_operation_count                               numeric(6, 0)     NOT NULL,
    reference_year_count                                  numeric(1, 0),
    total_income                                          numeric(16, 2)    NOT NULL,
    allowable_income                                      numeric(16, 2)    NOT NULL,
    allowable_expenses                                    numeric(16, 2)    NOT NULL,
    allowable_expenses_per_100                            numeric(16, 2)    NOT NULL,
    allowable_expenses_per_100_5_year                     numeric(16, 2),
    non_allowable_income                                  numeric(16, 2)    NOT NULL,
    non_allowable_expenses                                numeric(16, 2)    NOT NULL,
    non_allowable_expenses_per_100                        numeric(16, 2)    NOT NULL,
    non_allowable_expense_per_100_5_year                  numeric(16, 2),
    allowable_income_per_100                              numeric(16, 2)    NOT NULL,
    allowable_income_per_100_5_year                       numeric(16, 2),
    other_farm_income_per_100                             numeric(16, 2)    NOT NULL,
    other_farm_income_per_100_5_year                      numeric(16, 2),
    total_expenses_per_100                                numeric(16, 2)    NOT NULL,
    total_expenses_per_100_5_year                         numeric(16, 2),
    commodity_purchases_repayment_benefit_per_100         numeric(16, 2)    NOT NULL,
    commodity_purchases_repayment_per_100_5_year          numeric(16, 2),
    commodity_purchases_per_100_high_25_percent           numeric(16, 2)    NOT NULL,
    non_allowable_repayment_program_benefit_100           numeric(16, 2)    NOT NULL,
    non_allowable_repayment_program_benefit_100_5_year    numeric(16, 2),
    non_allowable_repayment_program_benefit_100_h25p      numeric(16, 2)    NOT NULL,
    net_margin                                            numeric(16, 2)    NOT NULL,
    net_margin_per_100                                    numeric(16, 2)    NOT NULL,
    net_margin_per_100_5_year                             numeric(16, 2),
    production_margin                                     numeric(16, 2)    NOT NULL,
    production_margin_per_100                             numeric(16, 2)    NOT NULL,
    production_margin_per_100_5_year                      numeric(16, 2),
    production_margin_ratio                               numeric(16, 3)    NOT NULL,
    production_margin_ratio_low_25_percentile             numeric(16, 3)    NOT NULL,
    operating_cost                                        numeric(16, 2)    NOT NULL,
    operating_cost_ratio                                  numeric(16, 3)    NOT NULL,
    operating_cost_ratio_high_25_percentile               numeric(16, 3)    NOT NULL,
    direct_expenses                                       numeric(16, 2)    NOT NULL,
    direct_expenses_ratio                                 numeric(16, 3)    NOT NULL,
    direct_expenses_ratio_high_25_percentile              numeric(16, 3)    NOT NULL,
    machinery_expenses                                    numeric(16, 2)    NOT NULL,
    machinery_expenses_ratio                              numeric(16, 3)    NOT NULL,
    machinery_expenses_ratio_high_25_percentile           numeric(16, 3)    NOT NULL,
    land_build_expenses                                   numeric(16, 2)    NOT NULL,
    land_build_expenses_ratio                             numeric(16, 3)    NOT NULL,
    land_build_expenses_ratio_high_25_percentile          numeric(16, 3)    NOT NULL,
    overhead_expenses                                     numeric(16, 2)    NOT NULL,
    overhead_expenses_ratio                               numeric(16, 3)    NOT NULL,
    overhead_expenses_ratio_high_25_percentile            numeric(16, 3)    NOT NULL,
    reference_year_indicator                              varchar(1)        NOT NULL,
    parent_tip_benchmark_year_id                          numeric(10, 0),
    generated_date                                        date              NOT NULL,
    revision_count                                        numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                           varchar(30)       NOT NULL,
    create_date                                           timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                           varchar(30),
    update_date                                           timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_benchmark_year.tip_benchmark_year_id IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.program_year IS 'PROGRAM YEAR is the PROGRAM YEAR this data pertains to.'
;
COMMENT ON COLUMN tip_benchmark_year.reference_year IS 'REFERENCE YEAR is the REFERENCE YEAR this data pertains to.'
;
COMMENT ON COLUMN tip_benchmark_year.farm_type_3_name IS 'FARM TYPE 3 NAME is a textual description of the third (and highest) level of TIP farm types.'
;
COMMENT ON COLUMN tip_benchmark_year.farm_type_2_name IS 'FARM TYPE 2 NAME is a textual description of the second (middle) level of TIP farm types.'
;
COMMENT ON COLUMN tip_benchmark_year.farm_type_1_name IS 'FARM TYPE 1 NAME is a textual description of the first (and lowest) level of TIP farm types.'
;
COMMENT ON COLUMN tip_benchmark_year.income_range_low IS 'INCOME RANGE LOW is the minimum range for the TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.income_range_high IS 'INCOME RANGE HIGH is the maximum range for the TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.minimum_group_count IS 'MINIMUM GROUP COUNT is the minimum number of farms required before any farms can be assigned to this TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.farming_operation_count IS 'FARMING OPERATION COUNT is the number of farming operations in the TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.reference_year_count IS 'REFERENCE YEAR COUNT is the number of years available to compute averages within the past five years (PROGRAM YEAR - 5 to PROGRAM YEAR - 1). For each benchmark group this will be equal to the MAX of REFERENCE YEAR COUNT from TIP REPORT RESULT. It will also equal the number of records in TIP BENCHMARK YEAR for the PARENT TIP BENCHMARK YEAR ID and where REFERENCE YEAR IND equals Y.'
;
COMMENT ON COLUMN tip_benchmark_year.total_income IS 'TOTAL INCOME is the total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_income IS 'ALLOWABLE INCOME is the total amount of allowable income of the farming operation.'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_expenses IS 'ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_expenses_per_100 IS 'ALLOWABLE EXPENSES PER 100 is the total amount of allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_expenses_per_100_5_year IS 'ALLOWABLE EXPENSES PER 100 5 YEAR is the 5-year average of the total amount of allowable expenses per $100 of Gross Farming Income (GFI) for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_income IS 'NON ALLOWABLE INCOME is the total amount of non-allowable income of the farming operation.'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_expenses IS 'NON ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_expenses_per_100 IS 'NON ALLOWABLE EXPENSES PER 100 is the amount of non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_expense_per_100_5_year IS 'NON ALLOWABLE EXPENSE PER 100 5 YEAR is the 5-year average of the NON ALLOWABLE EXPENSES PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_income_per_100 IS 'ALLOWABLE INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.allowable_income_per_100_5_year IS 'ALLOWABLE INCOME PER 100 5 YEAR is the 5-year average of the total amount of allowable income per $100 of Gross Farming Income (GFI) for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.other_farm_income_per_100 IS 'OTHER FARM INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.other_farm_income_per_100_5_year IS 'OTHER FARM INCOME PER100 5 YEAR is the 5-year average of the total amount of allowable income per $100 of Gross Farming Income (GFI) for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.total_expenses_per_100 IS 'TOTAL EXPENSES PER 100 is the total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.total_expenses_per_100_5_year IS 'TOTAL EXPENSES PER 100 5 YEAR is the 5-year average of the TOTAL EXPENSES PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.commodity_purchases_repayment_benefit_per_100 IS 'COMMODITY PURCHASES REPAYMENT BENEFIT PER 100 is the total amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.commodity_purchases_repayment_per_100_5_year IS 'COMMODITY PURCHASES REPAYMENT PER 100 5 YEAR is the 5-year average of COMMODITY PURCHASES REPAYMENT BENEFIT PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.commodity_purchases_per_100_high_25_percent IS 'COMMODITY PURCHASES PER 100 HIGH 25 PERCENT is the higher 25th percentile of COMMODITY PURCHASES REPAYMENT BENEFIT PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_repayment_program_benefit_100 IS 'NON ALLOWABLE REPAYMENT PROGRAM BENEFIT 100 is the total amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_repayment_program_benefit_100_5_year IS 'NON ALLOWABLE REPAYMENT PROGRAM BENEFIT 100 5 YEAR is the 5-year average of the NON ALLOWABLE REPAYMENT PROGRAM BENEFIT 100 5 YEAR this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.non_allowable_repayment_program_benefit_100_h25p IS 'NON ALLOWABLE REPAYMENT PROGRAM BENEFIT 100 H25P is the higher 25th percentile of NON ALLOWABLE REPAYMENT PROGRAM BENEFIT 100 H25P for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.net_margin IS 'NET MARGIN median allowable income minus allowable and non-allowable expenses.'
;
COMMENT ON COLUMN tip_benchmark_year.net_margin_per_100 IS 'NET MARGIN PER 100 median allowable income minus allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.net_margin_per_100_5_year IS 'NET MARGIN PER 100 5 YEAR is the 5-year average of NET MARGIN PER 100.'
;
COMMENT ON COLUMN tip_benchmark_year.production_margin IS 'PRODUCTION MARGIN is the margin amount (ALLOWABLE INCOME minus ALLOWABLE EXPENSES).'
;
COMMENT ON COLUMN tip_benchmark_year.production_margin_per_100 IS 'PRODUCTION MARGIN PER 100 is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI) and multiplied by 100.'
;
COMMENT ON COLUMN tip_benchmark_year.production_margin_per_100_5_year IS 'PRODUCTION MARGIN PER 100 5 YEAR is the 5-year average of the PRODUCTION MARGIN PER 100 for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.production_margin_ratio IS 'PRODUCTION MARGIN RATIO is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.production_margin_ratio_low_25_percentile IS 'PRODUCTION MARGIN RATIO LOW 25 PERCENTILE is the lower 25th percentile of PRODUCTION MARGIN RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.operating_cost IS 'OPERATING COST is ALLOWABLE EXPENSES minus amounts for depreciation expense codes.'
;
COMMENT ON COLUMN tip_benchmark_year.operating_cost_ratio IS 'OPERATING COST RATIO is the OPERATING COST divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.operating_cost_ratio_high_25_percentile IS 'OPERATING COST RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OPERATING COST RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.direct_expenses IS 'DIRECT EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_benchmark_year.direct_expenses_ratio IS 'DIRECT EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.direct_expenses_ratio_high_25_percentile IS 'DIRECT EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of DIRECT EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.machinery_expenses IS 'MACHINERY EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_benchmark_year.machinery_expenses_ratio IS 'MACHINERY EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.machinery_expenses_ratio_high_25_percentile IS 'MACHINERY EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of MACHINERY EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.land_build_expenses IS 'LAND BUILD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commissions, and twine.'
;
COMMENT ON COLUMN tip_benchmark_year.land_build_expenses_ratio IS 'LAND BUILD EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.land_build_expenses_ratio_high_25_percentile IS 'LAND BUILD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of LAND BUILD EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.overhead_expenses IS 'OVERHEAD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.'
;
COMMENT ON COLUMN tip_benchmark_year.overhead_expenses_ratio IS 'OVERHEAD EXPENSES RATIO is the OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI).'
;
COMMENT ON COLUMN tip_benchmark_year.overhead_expenses_ratio_high_25_percentile IS 'OVERHEAD EXPENSES RATIO HIGH 25 PERCENTILE is the higher 25th percentile of OVERHEAD EXPENSES RATIO for this benchmark group.'
;
COMMENT ON COLUMN tip_benchmark_year.reference_year_indicator IS 'REFERENCE YEAR INDICATOR indicates that this record is for a reference year (not the program year).'
;
COMMENT ON COLUMN tip_benchmark_year.parent_tip_benchmark_year_id IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN tip_benchmark_year.generated_date IS 'GENERATED DATE identifies the date and time that the benchmark was generated.'
;
COMMENT ON COLUMN tip_benchmark_year.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_benchmark_year.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_benchmark_year.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_benchmark_year.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_benchmark_year.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_benchmark_year IS 'TIP BENCHMARK YEAR contains the results of a TIP Report run against a FARMING OPERATION. The TIP Report compares income and expenses of an operation to an industry benchmark.'
;


CREATE INDEX ix_tby_py_ft3n ON tip_benchmark_year(program_year, farm_type_3_name)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tby_py_ry_ft3n_ft2n_ft1n_irh ON tip_benchmark_year(program_year, reference_year, farm_type_3_name, farm_type_2_name, farm_type_1_name, income_range_high)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tby_py_ry_ft3n_ft2n_ft1n_irl ON tip_benchmark_year(program_year, reference_year, farm_type_3_name, farm_type_2_name, farm_type_1_name, income_range_low)
 TABLESPACE pg_default
;

ALTER TABLE tip_benchmark_year ADD 
    CONSTRAINT pk_tby PRIMARY KEY (tip_benchmark_year_id) USING INDEX TABLESPACE pg_default 
;
