CREATE TABLE farms.farm_tip_benchmark_years (
	tip_benchmark_year_id bigint NOT NULL,
	program_year smallint NOT NULL,
	reference_year smallint NOT NULL,
	farm_type_3_name varchar(256),
	farm_type_2_name varchar(256),
	farm_type_1_name varchar(256),
	income_range_low decimal(12,2) NOT NULL,
	income_range_high decimal(12,2) NOT NULL,
	minimum_group_count smallint NOT NULL,
	farming_operation_count integer NOT NULL,
	reference_year_count smallint,
	total_income decimal(16,2) NOT NULL,
	allowable_income decimal(16,2) NOT NULL,
	allowable_expenses decimal(16,2) NOT NULL,
	allowable_expenses_per_100 decimal(16,2) NOT NULL,
	allowable_expenses_per_100_5yr decimal(16,2),
	non_allowable_income decimal(16,2) NOT NULL,
	non_allowable_expenses decimal(16,2) NOT NULL,
	non_allowable_expenses_per_100 decimal(16,2) NOT NULL,
	non_allowable_expn_per_100_5yr decimal(16,2),
	allowable_income_per_100 decimal(16,2) NOT NULL,
	allowable_income_per_100_5year decimal(16,2),
	other_farm_income_per100 decimal(16,2) NOT NULL,
	other_farm_income_per100_5year decimal(16,2),
	total_expenses_per_100 decimal(16,2) NOT NULL,
	total_expenses_per_100_5year decimal(16,2),
	cmmdty_prchs_rpay_bnft_per_100 decimal(16,2) NOT NULL,
	cmmdty_prchs_rpay_per_100_5yr decimal(16,2),
	cmmdty_prchs_per100_high_25pct decimal(16,2) NOT NULL,
	non_allowbl_repay_pgm_bnft_100 decimal(16,2) NOT NULL,
	non_allw_rpay_pgm_bnft_100_5yr decimal(16,2),
	non_allw_rpay_pgm_bnf_100_h25p decimal(16,2) NOT NULL,
	net_margin decimal(16,2) NOT NULL,
	net_margin_per_100 decimal(16,2) NOT NULL,
	net_margin_per_100_5year decimal(16,2),
	production_margin decimal(16,2) NOT NULL,
	production_margin_per_100 decimal(16,2) NOT NULL,
	production_margin_per_100_5yr decimal(16,2),
	production_margin_ratio decimal(16,3) NOT NULL,
	prodtn_margin_ratio_low_25pct decimal(16,3) NOT NULL,
	operating_cost decimal(16,2) NOT NULL,
	operating_cost_ratio decimal(16,3) NOT NULL,
	operatng_cost_ratio_high_25pct decimal(16,3) NOT NULL,
	direct_expenses decimal(16,2) NOT NULL,
	direct_expenses_ratio decimal(16,3) NOT NULL,
	direct_expens_ratio_high_25pct decimal(16,3) NOT NULL,
	machinery_expenses decimal(16,2) NOT NULL,
	machinery_expenses_ratio decimal(16,3) NOT NULL,
	machnry_expns_ratio_high_25pct decimal(16,3) NOT NULL,
	land_build_expenses decimal(16,2) NOT NULL,
	land_build_expenses_ratio decimal(16,3) NOT NULL,
	land_bld_expn_ratio_high_25pct decimal(16,3) NOT NULL,
	overhead_expenses decimal(16,2) NOT NULL,
	overhead_expenses_ratio decimal(16,3) NOT NULL,
	overhead_expn_ratio_high_25pct decimal(16,3) NOT NULL,
	reference_year_ind varchar(1) NOT NULL,
	parent_tip_benchmark_year_id bigint,
	generated_date timestamp(0) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_benchmark_years IS E'TIP BENCHMARK YEAR contains the results of a TIP Report run against a FARMING OPERATION. The TIP Report compares income and expenses of an operation to an industry benchmark.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_expenses IS E'ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_expenses_per_100 IS E'ALLOWABLE EXPENSES PER 100 is the total amount of allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_expenses_per_100_5yr IS E'ALLOWABLE EXPENSES PER 100 5YR is the 5-year average of the total amount of allowable expenses per $100 of Gross Farming Income (GFI) for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_income IS E'ALLOWABLE INCOME is the total amount of allowable income of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_income_per_100 IS E'ALLOWABLE INCOME PER 100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.allowable_income_per_100_5year IS E'ALLOWABLE INCOME PER 100 5YEAR is the 5-year average of the total amount of allowable income per $100 of Gross Farming Income (GFI) for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.cmmdty_prchs_per100_high_25pct IS E'CMMDTY PRCHS PER100 HIGH 25PCT is the higher 25th percentile of CMMDTY PRCHS RPAY BNFT PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.cmmdty_prchs_rpay_bnft_per_100 IS E'CMMDTY PRCHS RPAY BNFT PER 100 is the total amount of commodity purchases and repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.cmmdty_prchs_rpay_per_100_5yr IS E'CMMDTY PRCHS RPAY PER 100 5YR is the 5-year average of CMMDTY PRCHS RPAY BNFT PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.direct_expens_ratio_high_25pct IS E'DIRECT EXPENS RATIO HIGH 25PCT is the higher 25th percentile of DIRECT EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.direct_expenses IS E'DIRECT EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.direct_expenses_ratio IS E'DIRECT EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.farm_type_1_name IS E'FARM TYPE 1 NAME is a textual description of the first (and lowest) level of TIP farm types.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.farm_type_2_name IS E'FARM TYPE 2 NAME is a textual description of the second (middle) level of TIP farm types.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.farm_type_3_name IS E'FARM TYPE 3 NAME is a textual description of the third (and highest) level of TIP farm types.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.farming_operation_count IS E'FARMING OPERATION COUNT is the number of farming operations in the TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.generated_date IS E'GENERATED DATE identifies the date and time that the benchmark was generated.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.income_range_high IS E'INCOME RANGE HIGH is the maximum range for the TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.income_range_low IS E'INCOME RANGE LOW is the minimum range for the TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.land_bld_expn_ratio_high_25pct IS E'LAND BLD EXPN RATIO HIGH 25PCT is the higher 25th percentile of LAND BUILD EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.land_build_expenses IS E'LAND BUILD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.land_build_expenses_ratio IS E'LAND BUILD EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.machinery_expenses IS E'MACHINERY EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.machinery_expenses_ratio IS E'MACHINERY EXPENSES RATIO is the DIRECT EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.machnry_expns_ratio_high_25pct IS E'MACHNRY EXPNS RATIO HIGH 25PCT is the higher 25th percentile of MACHINERY EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.minimum_group_count IS E'MINIMUM GROUP COUNT is the minimum number of farms required before any farms can be assigned to this TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.net_margin IS E'NET MARGIN median allowable income minus allowable and non-allowable expenses.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.net_margin_per_100 IS E'NET MARGIN PER 100 median allowable income minus allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.net_margin_per_100_5year IS E'NET MARGIN PER 100 5YEAR is the 5-year average of NET MARGIN PER 100.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allowable_expenses IS E'NON ALLOWABLE EXPENSES is the total amount of non-allowable expenses of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allowable_expenses_per_100 IS E'NON ALLOWABLE EXPENSES PER 100 is the amount of non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allowable_expn_per_100_5yr IS E'NON ALLOWABLE EXPN PER 100 5YR is the 5-year average of the NON ALLOWABLE EXPENSES PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allowable_income IS E'NON ALLOWABLE INCOME is the total amount of non-allowable income of the farming operation.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allowbl_repay_pgm_bnft_100 IS E'NON ALLOWBL REPAY PGM BNFT 100 is the total amount of non-allowable repayment of program benefits expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allw_rpay_pgm_bnf_100_h25p IS E'NON ALLW RPAY PGM BNF 100 H25P is the higher 25th percentile of NON ALLW RPAY PGM BNF 100 H25P for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.non_allw_rpay_pgm_bnft_100_5yr IS E'NON ALLW RPAY PGM BNFT 100 5YR is the 5-year average of the NON ALLW RPAY PGM BNFT 100 5YR this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.operating_cost IS E'OPERATING COST is ALLOWABLE EXPENSES minus amounts for depreciation expense codes.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.operating_cost_ratio IS E'OPERATING COST RATIO is the OPERATING COST divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.operatng_cost_ratio_high_25pct IS E'OPERATNG COST RATIO HIGH 25PCT is the higher 25th percentile of OPERATING COST RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.other_farm_income_per100 IS E'OTHER FARM INCOME PER100 is the total amount of allowable income per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.other_farm_income_per100_5year IS E'OTHER FARM INCOME PER100 5YEAR is the 5-year average of the total amount of allowable income per $100 of Gross Farming Income (GFI) for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.overhead_expenses IS E'OVERHEAD EXPENSES is expenses for seed, fertilizer, pesticides, livestock purchases, feed, veterinary, production insurance, soil testing, commisions, and twine.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.overhead_expenses_ratio IS E'OVERHEAD EXPENSES RATIO is the OVERHEAD EXPENSES divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.overhead_expn_ratio_high_25pct IS E'OVERHEAD EXPN RATIO HIGH 25PCT is the higher 25th percentile of OVERHEAD EXPENSES RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.parent_tip_benchmark_year_id IS E'PARENT TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.prodtn_margin_ratio_low_25pct IS E'PRODTN MARGIN RATIO LOW 25PCT is the lower 25th percentile of PRODUCTION MARGIN RATIO for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.production_margin IS E'PRODUCTION MARGIN is the margin amount (ALLOWABLE INCOME minus ALLOWABLE EXPENSES).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.production_margin_per_100 IS E'PRODUCTION MARGIN PER 100 is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI) and multiplied by 100.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.production_margin_per_100_5yr IS E'PRODUCTION MARGIN PER 100 5YR is the 5-year average of the PRODUCTION MARGIN PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.production_margin_ratio IS E'PRODUCTION MARGIN RATIO is the ALLOWABLE INCOME minus the ALLOWABLE EXPENSES and then divided by the TOTAL INCOME (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.program_year IS E'PROGRAM YEAR is the PROGRAM YEAR this data pertains to.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.reference_year IS E'REFERENCE YEAR is the REFERENCE YEAR this data pertains to.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.reference_year_count IS E'REFERENCE YEAR COUNT is the number of years available to compute averages within the past five years (PROGRAM YEAR - 5 to PROGRAM YEAR - 1). For each benchmark group this will be equal to the MAX of REFERENCE YEAR COUNT from TIP REPORT RESULT. It will also equal the number of records in TIP BENCHMARK YEAR for the PARENT TIP BENCHMARK YEAR ID and where REFERENCE YEAR IND equals Y.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.reference_year_ind IS E'REFERENCE YEAR IND indicates that this record is for a reference year (not the program year).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.tip_benchmark_year_id IS E'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.total_expenses_per_100 IS E'TOTAL EXPENSES PER 100 is the total amount of allowable and non-allowable expenses per $100 of Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.total_expenses_per_100_5year IS E'TOTAL EXPENSES PER 100 5YEAR is the 5-year average of the TOTAL EXPENSES PER 100 for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.total_income IS E'TOTAL INCOME is the total amount of allowable and non-allowable income of the farming operation. Also known as Gross Farming Income (GFI).';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_years.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_tby_py_i ON farms.farm_tip_benchmark_years (program_year, farm_type_3_name);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_low_uk UNIQUE (program_year,reference_year,farm_type_3_name,farm_type_2_name,farm_type_1_name,income_range_low);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_high_uk UNIQUE (program_year,reference_year,farm_type_3_name,farm_type_2_name,farm_type_1_name,income_range_high);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_pk PRIMARY KEY (tip_benchmark_year_id);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_ftn_chk CHECK (((CASE WHEN((farm_type_3_name IS NOT NULL AND farm_type_3_name::text <> '') AND farm_type_3_name::text <> '') THEN 1 ELSE 0 END) + (CASE WHEN((farm_type_2_name IS NOT NULL AND farm_type_2_name::text <> '') AND farm_type_2_name::text <> '') THEN 1 ELSE 0 END) + (CASE WHEN((farm_type_1_name IS NOT NULL AND farm_type_1_name::text <> '') AND farm_type_1_name::text <> '') THEN 1 ELSE 0 END)) = 1);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_high_low_chk CHECK (income_range_low < income_range_high);
ALTER TABLE farms.farm_tip_benchmark_years ADD CONSTRAINT farm_tby_ry_chk CHECK (reference_year_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN tip_benchmark_year_id SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN reference_year SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN income_range_low SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN income_range_high SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN minimum_group_count SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN farming_operation_count SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN total_income SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN allowable_income SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN allowable_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN allowable_expenses_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN non_allowable_income SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN non_allowable_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN non_allowable_expenses_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN allowable_income_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN other_farm_income_per100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN total_expenses_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN cmmdty_prchs_rpay_bnft_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN cmmdty_prchs_per100_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN non_allowbl_repay_pgm_bnft_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN non_allw_rpay_pgm_bnf_100_h25p SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN net_margin SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN net_margin_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN production_margin SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN production_margin_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN production_margin_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN prodtn_margin_ratio_low_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN operating_cost SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN operating_cost_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN operatng_cost_ratio_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN direct_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN direct_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN direct_expens_ratio_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN machinery_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN machinery_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN machnry_expns_ratio_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN land_build_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN land_build_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN land_bld_expn_ratio_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN overhead_expenses SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN overhead_expenses_ratio SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN overhead_expn_ratio_high_25pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN reference_year_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN generated_date SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_years ALTER COLUMN when_created SET NOT NULL;
