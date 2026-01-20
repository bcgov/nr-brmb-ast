CREATE TABLE farms.farm_tip_report_results (
	tip_report_result_id bigint NOT NULL,
	participant_pin integer NOT NULL,
	year smallint NOT NULL,
	alignment_key varchar(2) NOT NULL,
	operation_number smallint NOT NULL,
	partnership_pin integer NOT NULL,
	partnership_name varchar(42),
	partnership_percent decimal(7,4) NOT NULL,
	farm_type_3_name varchar(256) NOT NULL,
	farm_type_2_name varchar(256) NOT NULL,
	farm_type_1_name varchar(256) NOT NULL,
	farm_type_3_percent decimal(4,3),
	farm_type_2_percent decimal(4,3),
	farm_type_1_percent decimal(4,3),
	farm_type_3_threshold_met_ind varchar(1),
	farm_type_2_threshold_met_ind varchar(1),
	farm_type_1_threshold_met_ind varchar(1),
	farm_type_level smallint,
	income_range_low decimal(13,2),
	income_range_high decimal(13,2),
	group_farming_operation_count integer,
	reference_year_count smallint,
	total_income decimal(16,2) NOT NULL,
	total_expenses decimal(16,2) NOT NULL,
	allowable_income decimal(16,2) NOT NULL,
	allowable_expenses decimal(16,2) NOT NULL,
	non_allowable_income decimal(16,2) NOT NULL,
	non_allowable_expenses decimal(16,2) NOT NULL,
	commodity_income decimal(16,2) NOT NULL,
	commodity_purchases decimal(16,2) NOT NULL,
	allowable_repaymnt_prgm_benfts decimal(16,2) NOT NULL,
	non_allowable_repay_prgm_bnfts decimal(16,2) NOT NULL,
	commdity_purchases_repay_bnfts decimal(16,2) NOT NULL,
	total_income_benchmark decimal(16,2),
	allowable_income_per_100 decimal(16,2) NOT NULL,
	allowable_income_per_100_5year decimal(16,2),
	allowable_income_per_100_bench decimal(16,2),
	other_farm_income_per100 decimal(16,2) NOT NULL,
	other_farm_income_per100_5year decimal(16,2),
	other_farm_income_per100_bench decimal(16,2),
	allowable_expenses_per_100 decimal(16,2) NOT NULL,
	allowable_expens_per_100_5year decimal(16,2),
	allowable_expens_per_100_bench decimal(16,2),
	non_allowable_expenses_per_100 decimal(16,2),
	non_allowabl_expns_per_100_5yr decimal(16,2),
	non_allowabl_expns_per_100_bch decimal(16,2),
	production_margin_per_100 decimal(16,2) NOT NULL,
	production_margin_per_100_5yr decimal(16,2),
	production_margin_per_100_bch decimal(16,2),
	total_expenses_per_100 decimal(16,2) NOT NULL,
	total_expenses_per_100_5year decimal(16,2),
	total_expenses_per_100_bench decimal(16,2),
	cmmdty_prchs_rpay_bnft_100 decimal(16,2) NOT NULL,
	cmmdty_prchs_rpay_bnft_100_5yr decimal(16,2),
	cmmdty_prchs_rpay_bnft_100_bch decimal(16,2),
	cmmdty_prchs_per100_high_25pct decimal(16,2),
	non_allowbl_repay_pgm_bnft_100 decimal(16,2) NOT NULL,
	non_allw_rpay_pgm_bnft_100_5yr decimal(16,2),
	non_allw_rpay_pgm_bnft_100_bch decimal(16,2),
	non_allw_rpay_pgm_bnf_100_h25p decimal(16,2),
	production_margin decimal(16,2) NOT NULL,
	production_margin_ratio decimal(18,4) NOT NULL,
	production_margin_ratio_5year decimal(18,4),
	production_margin_ratio_bench decimal(18,4),
	prodtn_margin_ratio_low_25pct decimal(16,2),
	net_margin decimal(16,2) NOT NULL,
	net_margin_per_100 decimal(16,2) NOT NULL,
	net_margin_per_100_5yr decimal(16,2),
	net_margin_per_100_bench decimal(16,2),
	operating_cost decimal(16,2) NOT NULL,
	operating_cost_ratio decimal(18,4) NOT NULL,
	operating_cost_ratio_5year decimal(18,4),
	operating_cost_ratio_bench decimal(18,4),
	operatng_cost_ratio_high_25pct decimal(16,2),
	direct_expenses decimal(16,2) NOT NULL,
	direct_expenses_ratio decimal(18,4) NOT NULL,
	direct_expenses_ratio_5year decimal(18,4),
	direct_expenses_ratio_bench decimal(18,4),
	direct_expens_ratio_high_25pct decimal(16,2),
	machinery_expenses decimal(16,2) NOT NULL,
	machinery_expenses_ratio decimal(18,4) NOT NULL,
	machinery_expenses_ratio_5year decimal(18,4),
	machinery_expenses_ratio_bench decimal(18,4),
	machnry_expns_ratio_high_25pct decimal(16,2),
	land_build_expenses decimal(16,2) NOT NULL,
	land_build_expenses_ratio decimal(18,4) NOT NULL,
	land_build_expens_ratio_5year decimal(18,4),
	land_build_expens_ratio_bench decimal(18,4),
	land_bld_expn_ratio_high_25pct decimal(16,2),
	overhead_expenses decimal(16,2) NOT NULL,
	overhead_expenses_ratio decimal(18,4) NOT NULL,
	overhead_expenses_ratio_5year decimal(18,4),
	overhead_expenses_ratio_bench decimal(18,4),
	overhead_expn_ratio_high_25pct decimal(16,2),
	use_for_benchmarks_ind varchar(1) NOT NULL,
	reference_year_ind varchar(1) NOT NULL,
	generated_date timestamp(0) NOT NULL,
	cmmdty_prchs_rpay_b_100_rating varchar(10),
	production_margin_ratio_rating varchar(10),
	operating_cost_ratio_rating varchar(10),
	direct_expenses_ratio_rating varchar(10),
	machinery_expenses_ratio_ratng varchar(10),
	land_build_expens_ratio_rating varchar(10),
	overhead_expenses_ratio_rating varchar(10),
	program_year_id bigint NOT NULL,
	farming_operation_id bigint NOT NULL,
	parent_tip_report_result_id bigint,
	tip_benchmark_year_id bigint,
	tip_benchmark_year_id_type_3 bigint,
	tip_benchmark_year_id_type_2 bigint,
	tip_benchmark_year_id_type_1 bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_report_results IS E'TIP REPORT RESULT contains the results of a TIP Report run against a FARMING OPERATION. The TIP Report compares income and expenses of an operation to an industry benchmark.';
COMMENT ON COLUMN farms.farm_tip_report_results.alignment_key IS E'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expens_per_100_5year IS E'ALLOWABLE EXPENS PER 100 5YEAR is the 5-year average total amount of allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expens_per_100_bench IS E'ALLOWABLE EXPENS PER 100 BENCH is the industry benchmark average total amount of allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expenses IS E'ALLOWABLE EXPENSES is the total amount of allowable income of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_expenses_per_100 IS E'ALLOWABLE EXPENSES PER 100 is the total amount of allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income IS E'ALLOWABLE INCOME is the total amount of allowable income of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100 IS E'ALLOWABLE INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100_5year IS E'ALLOWABLE INCOME PER 100 5YEAR is the 5-year average total amount of allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_income_per_100_bench IS E'ALLOWABLE INCOME PER 100 BENCH is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is an average of farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.allowable_repaymnt_prgm_benfts IS E'ALLOWABLE REPAYMNT PRGM BENFTS is the total allowable expenses for repayment of AgriStability benefits.';
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_per100_high_25pct IS E'CMMDTY PRCHS PER100 HIGH 25PCT is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_b_100_rating IS E'CMMDTY PRCHS RPAY B 100 RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100 IS E'CMMDTY PRCHS RPAY BNFT 100 is the amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100_5yr IS E'CMMDTY PRCHS RPAY BNFT 100 5YR is the 5-year average amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.cmmdty_prchs_rpay_bnft_100_bch IS E'CMMDTY PRCHS RPAY BNFT 100 BCH is the industry benchmark amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.commdity_purchases_repay_bnfts IS E'COMMDITY PURCHASES REPAY BNFTS is the total allowable expenses for for commodity purchases and repayment of AgriStability benefits.';
COMMENT ON COLUMN farms.farm_tip_report_results.commodity_income IS E'COMMODITY INCOME is the total income from commodity sales. All commodities have an entry in TIP LINE ITEMS where TIP_FARM_TYPE_1_LOOKUP_ID is not null.';
COMMENT ON COLUMN farms.farm_tip_report_results.commodity_purchases IS E'COMMODITY PURCHASES is the total expenses for commodity purchases. All commodities have an entry in TIP LINE ITEMS where TIP_FARM_TYPE_1_LOOKUP_ID is not null.';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expens_ratio_high_25pct IS E'DIRECT EXPENS RATIO HIGH 25PCT is the higher 25th percentile of DIRECT EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses IS E'DIRECT EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio IS E'DIRECT EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_5year IS E'DIRECT EXPENSES RATIO 5YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_bench IS E'DIRECT EXPENSES RATIO BENCH is the industry benchmark ratio of the DIRECT EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.direct_expenses_ratio_rating IS E'DIRECT EXPENSES RATIO RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_name IS E'FARM TYPE 1 NAME is the unique business name for the type of farm.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_percent IS E'FARM TYPE 1 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_1_threshold_met_ind IS E'FARM TYPE 1 THRESHOLD MET IND indicates whether the farming operation meets the threshhold required to be classified as this farm type.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_name IS E'FARM TYPE 2 NAME is the unique business name for the type of farm.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_percent IS E'FARM TYPE 2 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_2_threshold_met_ind IS E'FARM TYPE 2 THRESHOLD MET IND indicates whether the farming operation meets the threshhold required to be classified as this farm type.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_name IS E'FARM TYPE 3 NAME is the unique business name for the type of farm.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_percent IS E'FARM TYPE 3 PERCENT is the percentage of commodity income that belongs to this farm type, for the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_3_threshold_met_ind IS E'FARM TYPE 3 THRESHOLD MET IND indicates whether the farming operation meets the threshhold required to be classified as this farm type.';
COMMENT ON COLUMN farms.farm_tip_report_results.farm_type_level IS E'FARM TYPE LEVEL is the level of farm type that will be used to generate bencharks and compare with other farms. Possible values 1, 2, or 3.';
COMMENT ON COLUMN farms.farm_tip_report_results.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_tip_report_results.generated_date IS E'GENERATED DATE identifies the date and time that the TIP Report result was generated.';
COMMENT ON COLUMN farms.farm_tip_report_results.group_farming_operation_count IS E'GROUP FARMING OPERATION COUNT is the number of operations in the FARM TYPE LEVEL with income between INCOME RANGE LOW and INCOME RANGE HIGH.';
COMMENT ON COLUMN farms.farm_tip_report_results.income_range_high IS E'INCOME RANGE HIGH is the maximum range for a grouping for a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_report_results.income_range_low IS E'INCOME RANGE LOW is the minimum range for a grouping for a TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_report_results.land_bld_expn_ratio_high_25pct IS E'LAND BLD EXPN RATIO HIGH 25PCT is the higher 25th percentile of LAND BUILD RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_5year IS E'LAND BUILD EXPENS RATIO 5YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).';
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_bench IS E'LAND BUILD EXPENS RATIO BENCH is the industry benchmark ratio of the LAND BUILD RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expens_ratio_rating IS E'LAND BUILD RATIO RATNG is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expenses IS E'LAND BUILD is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_report_results.land_build_expenses_ratio IS E'LAND BUILD RATIO is the LAND BUILD divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses IS E'MACHINERY EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio IS E'MACHINERY EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_5year IS E'MACHINERY EXPENSES RATIO 5YEAR is the 5-year average of (DIRECT EXPENSES divided by the TOTAL INCOME (GFI)).';
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_bench IS E'MACHINERY EXPENSES RATIO BENCH is the industry benchmark ratio of the MACHINERY EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.machinery_expenses_ratio_ratng IS E'MACHINERY EXPENSES RATIO RATNG is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.machnry_expns_ratio_high_25pct IS E'MACHNRY EXPNS RATIO HIGH 25PCT is the higher 25th percentile of MACHINERY EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin IS E'NET MARGIN is the margin amount (TOTAL INCOME minus TOTAL EXPENSES).';
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100 IS E'NET MARGIN PER 100 is the NET MARGIN divided by the TOTAL INCOME (GFI) and multiplied by 100.';
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100_5yr IS E'NET MARGIN PER 100 5YR is the 5-year average of NET MARGIN PER 100.';
COMMENT ON COLUMN farms.farm_tip_report_results.net_margin_per_100_bench IS E'NET MARGIN PER 100 BENCH is the median NET MARGIN PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowabl_expns_per_100_5yr IS E'NON ALLOWABL EXPNS PER 100 5YR is the 5-year average amount of non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowabl_expns_per_100_bch IS E'NON ALLOWABL EXPNS PER 100 BCH is the industry benchmark amount of non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_expenses IS E'NON ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_expenses_per_100 IS E'NON ALLOWABLE EXPENSES PER 100 is the amount of non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_income IS E'NON ALLOWABLE INCOME is the total amount of non-allowable income of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowable_repay_prgm_bnfts IS E'NON ALLOWABLE REPAY PRGM BNFTS is the total non-allowable expenses for repayment of AgriStability benefits.';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allowbl_repay_pgm_bnft_100 IS E'NON_ALLOWBL_REPAY_PGM_BNFT_100 is the amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnf_100_h25p IS E'CMMDTY PRCHS PER100 HIGH 25PCT is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnft_100_5yr IS E'NON_ALLW_RPAY_PGM_BNFT_100_5YR is the 5-year average amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.non_allw_rpay_pgm_bnft_100_bch IS E'CMMDTY PRCHS RPAY BNFT 100 BCH is the industry benchmark amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost IS E'OPERATING COST is ALLOWABLE EXPENSES minus amounts for depreciation expense codes.';
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio IS E'OPERATING COST RATIO is the OPERATING COST divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_5year IS E'OPERATING COST RATIO 5YEAR is the 5-year average OPERATING COST divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_bench IS E'OPERATING COST RATIO BENCH is the industry benchmark ratio of the OPERATING COST divided by the TOTAL INCOME (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.operating_cost_ratio_rating IS E'OPERATING COST RATIO RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given year. Operations may have different OPERATION NUMBER in different years.';
COMMENT ON COLUMN farms.farm_tip_report_results.operatng_cost_ratio_high_25pct IS E'OPERATNG COST RATIO HIGH 25PCT is the higher 25th percentile of OPERATING COST RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per100 IS E'OTHER FARM INCOME PER100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per100_5year IS E'OTHER FARM INCOME PER100 5YEAR is the 5-year average total amount of non-allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.other_farm_income_per100_bench IS E'OTHER FARM INCOME PER100 BENCH is the industry benchmark total amount of non-allowable income per $100 of Gross Farming Income (GFI). The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses IS E'OVERHEAD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio IS E'OVERHEAD EXPENSES RATIO is the OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_5year IS E'OVERHEAD EXPENSES RATIO 5YEAR is the 5-year average of (OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI)).';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_bench IS E'OVERHEAD EXPENSES RATIO BENCH is the industry benchmark ratio of the OVERHEAD EXPENSES RATIO. The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expenses_ratio_rating IS E'OVERHEAD EXPENSES RATIO RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.overhead_expn_ratio_high_25pct IS E'OVERHEAD EXPN RATIO HIGH 25PCT is the higher 25th percentile of OVERHEAD EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.parent_tip_report_result_id IS E'PARENT TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT. If null then this is a parent record (program year). If not null then this record is for is a reference year.';
COMMENT ON COLUMN farms.farm_tip_report_results.participant_pin IS E'PARTICIPANT PIN (Personal Information Number) is the unique AgriStability/AgriInvest pin for this producuer. Was previously CAIS PIN and NISA PIN.';
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_name IS E'PARTNERSHIP NAME is the name of the partnership for the FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_percent IS E'PARTNERSHIP PERCENT is the Percentage of the  Partnership (100% will be stored as 1.0).';
COMMENT ON COLUMN farms.farm_tip_report_results.partnership_pin IS E'PARTNERSHIP PIN is a unique identifier for a farming operation parternership.';
COMMENT ON COLUMN farms.farm_tip_report_results.prodtn_margin_ratio_low_25pct IS E'PRODTN MARGIN RATIO LOW 25PCT is the higher 25th percentile of higher 25th percentile of PRODUCTION MARGIN RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin IS E'PRODUCTION MARGIN is the margin amount (ALLOWABLE INCOME minus ALLOWABLE EXPENSES).';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100 IS E'PRODUCTION MARGIN PER 100 is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI) and multiplied by 100.';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100_5yr IS E'PRODUCTION MARGIN PER 100 5YR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_per_100_bch IS E'PRODUCTION MARGIN PER 100 BCH is the production_margin_per_100_bch of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio IS E'PRODUCTION MARGIN RATIO is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_5year IS E'PRODUCTION MARGIN RATIO 5YEAR is the 5-year average of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME) (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_bench IS E'PRODUCTION MARGIN RATIO BENCH is the industry benchmark ratio of (ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME). The benchmark is the average for farming operations with similar revenues and commodities sold.';
COMMENT ON COLUMN farms.farm_tip_report_results.production_margin_ratio_rating IS E'PRODUCTION MARGIN RATIO RATING is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.';
COMMENT ON COLUMN farms.farm_tip_report_results.program_year_id IS E'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_tip_report_results.reference_year_count IS E'REFERENCE YEAR COUNT is the number of years available to compute averages within the past five years (YEAR - 5 to YEAR - 1).';
COMMENT ON COLUMN farms.farm_tip_report_results.reference_year_ind IS E'REFERENCE YEAR IND indicates that this record is for a reference year (not the program year).';
COMMENT ON COLUMN farms.farm_tip_report_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id IS E'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR. This is the TIP BENCHMARK YEAR that this record belongs to for FARM TYPE indicated by FARM TYPE LEVEL (1, 2, or 3).';
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_1 IS E'TIP BENCHMARK YEAR ID TYPE 1 is a surrogate unique identifier for TIP BENCHMARK YEAR. This is the TIP BENCHMARK YEAR that this record belongs to for TIP FARM TYPE 1.';
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_2 IS E'TIP BENCHMARK YEAR ID TYPE 2 is a surrogate unique identifier for TIP BENCHMARK YEAR. This is the TIP BENCHMARK YEAR that this record belongs to for TIP FARM TYPE 2.';
COMMENT ON COLUMN farms.farm_tip_report_results.tip_benchmark_year_id_type_3 IS E'TIP BENCHMARK YEAR ID TYPE 3 is a surrogate unique identifier for TIP BENCHMARK YEAR. This is the TIP BENCHMARK YEAR that this record belongs to for TIP FARM TYPE 3.';
COMMENT ON COLUMN farms.farm_tip_report_results.tip_report_result_id IS E'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.';
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses IS E'TOTAL EXPENSES is the total amount of allowable and non-allowable expenses of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100 IS E'TOTAL EXPENSES PER 100 is the total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100_5year IS E'TOTAL EXPENSES PER 100 5YEAR is the 5-year average total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.total_expenses_per_100_bench IS E'TOTAL EXPENSES PER 100 BENCH is the industry benchmark total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.total_income IS E'TOTAL INCOME is the total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.total_income_benchmark IS E'TOTAL INCOME BENCHMARK is the industry benchmark total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_report_results.use_for_benchmarks_ind IS E'USE FOR BENCHMARKS IND indicates that this record should be used when calculating benchmarks rather than the others with the same partnership PIN. Prevents counting the same operation/partnership multiple times.';
COMMENT ON COLUMN farms.farm_tip_report_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_report_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_report_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_report_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_report_results.year IS E'YEAR is the program year or reference year this data pertains to.';
CREATE INDEX farm_trr_farm_trc_cprb_fk_i ON farms.farm_tip_report_results (cmmdty_prchs_rpay_b_100_rating);
CREATE INDEX farm_trr_farm_trc_derr_fk_i ON farms.farm_tip_report_results (direct_expenses_ratio_rating);
CREATE INDEX farm_trr_farm_trc_lbrr_fk_i ON farms.farm_tip_report_results (land_build_expens_ratio_rating);
CREATE INDEX farm_trr_farm_trc_merr_fk_i ON farms.farm_tip_report_results (machinery_expenses_ratio_ratng);
CREATE INDEX farm_trr_farm_trc_ocrr_fk ON farms.farm_tip_report_results (operating_cost_ratio_rating);
CREATE INDEX farm_trr_farm_trc_oerr_fk_i ON farms.farm_tip_report_results (overhead_expenses_ratio_rating);
CREATE INDEX farm_trr_farm_trc_pmrr_fk_i ON farms.farm_tip_report_results (production_margin_ratio_rating);
CREATE INDEX farm_trr_farm_trr_fk_i ON farms.farm_tip_report_results (parent_tip_report_result_id);
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_fo_uk UNIQUE (farming_operation_id,parent_tip_report_result_id);
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_py_ak_uk UNIQUE (program_year_id,alignment_key,parent_tip_report_result_id);
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_pk PRIMARY KEY (tip_report_result_id);
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ft1i_chk CHECK (farm_type_1_threshold_met_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ft2i_chk CHECK (farm_type_2_threshold_met_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ft3i_chk CHECK (farm_type_3_threshold_met_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ftn_chk CHECK (((parent_tip_report_result_id IS NOT NULL AND parent_tip_report_result_id::text <> '') AND parent_tip_report_result_id::text <> '')
         or (    ((farm_type_3_percent IS NOT NULL AND farm_type_3_percent::text <> '') AND farm_type_3_percent::text <> '')
          and ((farm_type_2_percent IS NOT NULL AND farm_type_2_percent::text <> '') AND farm_type_2_percent::text <> '')
          and ((farm_type_1_percent IS NOT NULL AND farm_type_1_percent::text <> '') AND farm_type_1_percent::text <> '')
          and ((farm_type_3_threshold_met_ind IS NOT NULL AND farm_type_3_threshold_met_ind::text <> '') AND farm_type_3_threshold_met_ind::text <> '')
          and ((farm_type_2_threshold_met_ind IS NOT NULL AND farm_type_2_threshold_met_ind::text <> '') AND farm_type_2_threshold_met_ind::text <> '')
          and ((farm_type_1_threshold_met_ind IS NOT NULL AND farm_type_1_threshold_met_ind::text <> '') AND farm_type_1_threshold_met_ind::text <> '')));
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ry_chk CHECK (reference_year_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_report_results ADD CONSTRAINT farm_trr_ufb_chk CHECK (use_for_benchmarks_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN tip_report_result_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN year SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN alignment_key SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN partnership_pin SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN partnership_percent SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN farm_type_3_name SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN farm_type_2_name SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN farm_type_1_name SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN total_income SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN total_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN allowable_income SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN allowable_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN non_allowable_income SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN non_allowable_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN commodity_income SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN commodity_purchases SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN allowable_repaymnt_prgm_benfts SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN non_allowable_repay_prgm_bnfts SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN commdity_purchases_repay_bnfts SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN allowable_income_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN other_farm_income_per100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN allowable_expenses_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN production_margin_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN total_expenses_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN cmmdty_prchs_rpay_bnft_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN non_allowbl_repay_pgm_bnft_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN production_margin SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN production_margin_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN net_margin SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN net_margin_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN operating_cost SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN operating_cost_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN direct_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN direct_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN machinery_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN machinery_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN land_build_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN land_build_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN overhead_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN overhead_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN use_for_benchmarks_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN reference_year_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN generated_date SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN program_year_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_report_results ALTER COLUMN when_created SET NOT NULL;
