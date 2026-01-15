CREATE TABLE farms.farm_reasonabilty_test_results (
	reasonability_test_result_id bigint NOT NULL,
	fresh_ind varchar(1) NOT NULL,
	generated_date timestamp(0) NOT NULL,
	forage_consumer_capacity decimal(19,3),
	benefit_risk_pass_ind varchar(1) NOT NULL,
	program_year_margin decimal(13,2) NOT NULL,
	total_benefit decimal(13,2) NOT NULL,
	benchmark_margin decimal(25,2),
	benchmark_margin_deductible decimal(13,3) NOT NULL,
	benchmark_margin_less_deductbl decimal(25,2),
	benchmark_margin_less_py_marg decimal(25,2),
	benchmark_margin_payout_level decimal(13,3) NOT NULL,
	benefit_benchmark_befor_combnd decimal(25,2) NOT NULL,
	combined_benefit_percent decimal(4,3),
	benefit_benchmark decimal(25,2) NOT NULL,
	benefit_risk_variance decimal(25,3),
	benefit_risk_variance_limit decimal(16,3) NOT NULL,
	margin_pass_ind varchar(1) NOT NULL,
	adjusted_reference_margin decimal(13,2),
	adj_reference_margin_variance decimal(16,3),
	adj_reference_margin_vrinc_lmt decimal(16,3) NOT NULL,
	within_lmt_prct_ref_margin_ind varchar(1) NOT NULL,
	margin_industry_average decimal(13,2) NOT NULL,
	margin_industry_avg_variance decimal(16,3),
	margin_industry_average_limit decimal(16,3) NOT NULL,
	margin_wthn_lmt_indtry_avg_ind varchar(1) NOT NULL,
	structural_change_pass_ind varchar(1) NOT NULL,
	production_marg_accr_adjs decimal(16,2) NOT NULL,
	ratio_adj_reference_margin decimal(13,2) NOT NULL,
	additive_adj_reference_margin decimal(13,2) NOT NULL,
	ratio_additive_ref_marg_diff decimal(13,2) NOT NULL,
	ratio_add_diff_dollar_limit decimal(13,2) NOT NULL,
	within_dlr_limit_ratio_add_ind varchar(1) NOT NULL,
	additive_division_ratio decimal(16,3),
	additive_division_ratio_limit decimal(16,3) NOT NULL,
	within_limit_addtiv_divisn_ind varchar(1) NOT NULL,
	structual_change_method_to_use varchar(10) NOT NULL,
	farm_size_ratio_limit decimal(8,3) NOT NULL,
	farm_size_rtios_within_lmt_ind varchar(1) NOT NULL,
	revenue_risk_pass_ind varchar(1),
	revenue_risk_fruit_vg_pass_ind varchar(1),
	revenue_rsk_grain_frg_pass_ind varchar(1),
	revenue_rsk_grain_forage_limit decimal(13,2),
	expense_indtry_av_cmp_pass_ind varchar(1) NOT NULL,
	expense_accr_adjs_indtry_av_cm decimal(16,2) NOT NULL,
	expense_industry_average decimal(13,2) NOT NULL,
	expense_industry_avg_variance decimal(16,3),
	expense_industry_average_limit decimal(16,3) NOT NULL,
	expense_ref_year_comp_pass_ind varchar(1),
	expense_accr_adjs decimal(16,2) NOT NULL,
	expenses_aft_str_ch_yr_minus_1 decimal(13,2),
	expenses_aft_str_ch_yr_minus_2 decimal(13,2),
	expenses_aft_str_ch_yr_minus_3 decimal(13,2),
	expenses_aft_str_ch_yr_minus_4 decimal(13,2),
	expenses_aft_str_ch_yr_minus_5 decimal(13,2),
	expenses_aft_str_ch_ref_yr_avg decimal(13,2),
	expenses_aft_str_ch_variance decimal(16,2),
	expenses_aft_str_ch_varian_lmt decimal(16,2),
	production_pass_ind varchar(1),
	production_forage_fs_pass_ind varchar(1),
	prod_forage_qty_produced_limit decimal(16,3),
	production_grains_pass_ind varchar(1),
	prod_grain_qty_produced_limit decimal(16,3),
	production_fruit_veg_pass_ind varchar(1),
	prod_fruit_veg_qty_prodcd_limt decimal(16,3),
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_reasonabilty_test_results IS E'REASONABILTY TEST RESULT contains the results of reasonability tests run against the scenario. Reasonability tests are a set of automated tests that will flag a benefit if it falls outside expected boundaries.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_adj_reference_margin IS E'ADDITIVE ADJ REFERENCE MARGIN is the REFERENCE MARGIN using the Additive structural change method.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_division_ratio IS E'ADDITIVE DIVISION RATIO is calcualted by dividing RATIO ADJ REFERENCE MARGIN by ADDITIVE ADJ REFERENCE MARGIN.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.additive_division_ratio_limit IS E'ADDITIVE DIVISION RATIO LIMIT is the configured limit for the ADDITIVE DIVISION RATIO. If it is outside the limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adj_reference_margin_variance IS E'ADJ REFERENCE MARGIN VARIANCE is the percent difference between ADJUSTED REFERENCE MARGIN and the PROGRAM YEAR MARGIN.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adj_reference_margin_vrinc_lmt IS E'ADJ REFERENCE MARGIN VRINC LMT is the configured limit for the percentage difference difference between ADJUSTED REFERENCE MARGIN and the PROGRAM YEAR MARGIN. If the margins are outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.adjusted_reference_margin IS E'ADJUSTED REFERENCE MARGIN is the reference margin after accrual adjustments have been applied.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin IS E'BENCHMARK MARGIN is the total industry margin calculated as the PRODUCTIVE UNIT AMOUNT multiplied by the BPU 5-year average (taking missing years and Lead/Lag into account).';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_deductible IS E'BENCHMARK MARGIN DEDUCTIBLE is the deductible percentage of the margin not covered.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_less_deductbl IS E'BENCHMARK MARGIN LESS DEDUCTBL is BENCHMARK MARGIN minus the deductible amount.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_less_py_marg IS E'BENCHMARK MARGIN LESS PY MARG is BENCHMARK MARGIN LESS DEDUCTBL minus the PROGRAM YEAR MARGIN.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benchmark_margin_payout_level IS E'BENCHMARK MARGIN PAYOUT LEVEL is the percentage of BENCHMARK MARGIN LESS PY MARG that would be paid out.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_benchmark IS E'BENEFIT BENCHMARK is the industry benchmark that will be compared with the calculated benefit. If this is a combined farm then the Combined Farm % has been applied.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_benchmark_befor_combnd IS E'BENEFIT BENCHMARK BEFOR COMBND is BENCHMARK MARGIN LESS PY MARG multiplied by BENCHMARK MARGIN PAYOUT LEVEL. This is the value before Combined Farm % has been applied (combined farms only).';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_pass_ind IS E'BENEFIT RISK PASS IND denotes whether Benefit Risk Assessment passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_variance IS E'BENEFIT RISK VARIANCE is the percent difference between TOTAL BENEFIT and the BENEFIT BENCHMARK.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.benefit_risk_variance_limit IS E'BENEFIT RISK VARIANCE LIMIT is the configured limit for the percentage difference between the TOTAL BENEFIT and the BENEFIT BENCHMARK. If it is outside this limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.combined_benefit_percent IS E'COMBINED BENEFIT PERCENT is the percentage of the combined farm''s benefit that will be paid to this PIN/client (combined farms only).';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_accr_adjs IS E'EXPENSE ACCR ADJS identifies the expenses adjusted by accruals for the program year. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_accr_adjs_indtry_av_cm IS E'EXPENSE ACCR ADJS INDTRY AV CM identifies the expenses adjusted by accruals for the program year. For Expense Test - Industry Average Comparison, expense amounts for these codes are subtracted: 9661 - Containers and twine and 9836 - Commissions and Levies';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_indtry_av_cmp_pass_ind IS E'EXPENSE INDTRY AV CMP PASS IND denotes whether the Expense Test - Industry Average Comparison passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_average IS E'EXPENSE INDUSTRY AVERAGE is calculated by multiplying the average of the reference year BPU Expense Margins for each commodity produced.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_average_limit IS E'EXPENSE INDUSTRY AVERAGE LIMIT is the configured limit for the EXPENSE INDUSTRY AVG VARIANCE. If it is outside the limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_industry_avg_variance IS E'EXPENSE INDUSTRY AVG VARIANCE is calculated by subtracting the EXPENSE INDUSTRY AVERAGE from the EXPENSE ACCR ADJS and dividing result by the EXPENSE ACCR ADJS.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expense_ref_year_comp_pass_ind IS E'EXPENSE REF YEAR COMP PASS IND denotes whether the Expense Test - Reference Year Comparison passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_ref_yr_avg IS E'EXPENSES AFT STR CH REF YR AVG is the average expenses after structure changes of all the reference years.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_varian_lmt IS E'EXPENSES AFT STR CH VARIAN LMT is the percent difference between EXPENSE ACCR ADJS and EXPENSES AFT STR CH REF YR AVG.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_variance IS E'EXPENSES AFT STR CH VARIANCE is the percent difference between EXPENSE ACCR ADJS and EXPENSES AFT STR CH REF YR AVG.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_1 IS E'EXPENSES AFT STR CH YEAR MINUS 1 is the expenses after structure changes for the reference year that is program year minus 1.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_2 IS E'EXPENSES AFT STR CH YEAR MINUS 2 is the expenses after structure changes for the reference year that is program year minus 2.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_3 IS E'EXPENSES AFT STR CH YEAR MINUS 3 is the expenses after structure changes for the reference year that is program year minus 3.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_4 IS E'EXPENSES AFT STR CH YEAR MINUS 4 is the expenses after structure changes for the reference year that is program year minus 4.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.expenses_aft_str_ch_yr_minus_5 IS E'EXPENSES AFT STR CH YEAR MINUS 5 is the expenses after structure changes for the reference year that is program year minus 5.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.farm_size_ratio_limit IS E'FARM SIZE RATIO LIMIT is the configured limit for the farm size ratios for each reference year. If any of those are outside the limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.farm_size_rtios_within_lmt_ind IS E'FARM SIZE RTIOS WITHIN LMT IND indicates if the farm size ratios for each reference year were within the FARM SIZE RATIO LIMIT.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.forage_consumer_capacity IS E'FORAGE CONSUMER CAPACITY is the amount of forage inventory potentically consumed by (fed out to) cattle. The actual amount will be less if the capacity is greater than the the inventory.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.fresh_ind IS E'FRESH IND is used to determine if the test results are fresh. This means that they were run with the most recent changes to scenario which could change their results.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.generated_date IS E'GENERATED DATE identifies the date that the reasonability test result was generated.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_average IS E'MARGIN INDUSTRY AVERAGE is calculated by multiplying the average of the reference year BPUs for each commodity produced.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_average_limit IS E'MARGIN INDUSTRY AVERAGE LIMIT is the configured limit for the MARGIN INDUSTRY AVG VARIANCE. If it is outside the limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_industry_avg_variance IS E'MARGIN INDUSTRY AVG VARIANCE is calculated by subtracting the MARGIN INDUSTRY AVG VARIANCE from the PROGRAM YEAR MARGIN and dividing that by the PROGRAM YEAR MARGIN.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_pass_ind IS E'MARGIN PASS IND denotes whether the Margin Test passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.margin_wthn_lmt_indtry_avg_ind IS E'MARGIN WTHN LMT INDTRY AVG IND indicates if the MARGIN INDUSTRY AVG VARIANCE was within the MARGIN INDUSTRY AVERAGE LIMIT.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_forage_qty_produced_limit IS E'PROD FORAGE QTY PRODUCED LIMIT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN INVNTORY RSLT. If it is outside the limit, the Forage and Forage Seed Production test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_fruit_veg_qty_prodcd_limt IS E'PROD FRUIT VEG QTY PRODCD LIMT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN FRUT VEG RSLT. If it is outside the limit, the Perishables Production test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.prod_grain_qty_produced_limit IS E'PROD GRAIN QTY PRODUCED LIMIT is the configured limit for the QUANTITY PRODUCED VARIANCE in RSN PRDCTN GRAIN RESULT. If it is outside the limit, the Grains Production test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_forage_fs_pass_ind IS E'PRODUCTION FORAGE FS PASS IND denotes whether Production Test - Forage and Forage Seed passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_fruit_veg_pass_ind IS E'PRODUCTION FRUIT VEG PASS IND denotes whether Production Test - Perishable Crops passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_grains_pass_ind IS E'PRODUCTION GRAINS PASS IND denotes whether Production Test - Grains passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_marg_accr_adjs IS E'PRODUCTION MARG ACCR ADJS identifies the production margin associated with accrual adjustments.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.production_pass_ind IS E'PRODUCTION PASS IND denotes whether Production Test passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.program_year_margin IS E'PROGRAM YEAR MARGIN is determined by the Income less allowable expenses.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_add_diff_dollar_limit IS E'RATIO ADD DIFF DOLLAR LIMIT is the configured limit for the RATIO ADDITIVE REF MARG DIFF. If it is outside the limit, the test fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_additive_ref_marg_diff IS E'RATIO ADDITIVE REF MARG DIFF is calcualted by subtracting RATIO ADJ REFERENCE MARGIN from ADDITIVE ADJ REFERENCE MARGIN.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.ratio_adj_reference_margin IS E'RATIO ADJ REFERENCE MARGIN is the REFERENCE MARGIN using the Ratio structural change method.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.reasonability_test_result_id IS E'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_risk_fruit_vg_pass_ind IS E'REVENUE RISK FRUIT VG PASS IND denotes whether Revenue Risk - Fruits and Vegetables passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_risk_pass_ind IS E'REVENUE RISK PASS IND denotes whether Revenue Risk passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_rsk_grain_forage_limit IS E'REVENUE RSK GRAIN FORAGE LIMIT is the configured limit for the REVENUE VARIANCE LIMIT in RSN REV G F FS INCM RSLT. If any income codes (LINE ITEMs) have a variance outside this limit, the Revenue Risk - Grains, Forage, and Forage Seed fails.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revenue_rsk_grain_frg_pass_ind IS E'REVENUE RSK GRAIN FRG PASS IND denotes whether Revenue Risk - Grains, Forage, and Forage Seed passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.structual_change_method_to_use IS E'STRUCTUAL CHANGE METHOD TO USE is the Structural Change method that the test has determined should be used. If WITHIN DLR LIMIT RATIO ADD IND and WITHIN LIMIT ADDTIV DIVISN IND are Y then Ratio, if both are N then Additive, otherwise Manual selection.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.structural_change_pass_ind IS E'STRUCTURAL CHANGE PASS IND denotes whether the Structural Change Test passed.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.total_benefit IS E'TOTAL BENEFIT is the total calculated benefit prior to deductions.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_dlr_limit_ratio_add_ind IS E'WITHIN DLR LIMIT RATIO ADD IND indicates if the RATIO ADDITIVE REF MARG DIFF was within the RATIO ADD DIFF DOLLAR LIMIT.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_limit_addtiv_divisn_ind IS E'WITHIN LIMIT ADDTIV DIVISN IND indicates if the ADDITIVE DIVISION RATIO was within the ADDITIVE DIVISION RATIO LIMIT.';
COMMENT ON COLUMN farms.farm_reasonabilty_test_results.within_lmt_prct_ref_margin_ind IS E'WITHIN LMT PRCT REF MARGIN IND indicates whether the program year margin was within the configured limit of the reference year margins.';
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_uk UNIQUE (agristability_scenario_id);
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_pk PRIMARY KEY (reasonability_test_result_id);
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_brp_chk CHECK (benefit_risk_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_eiac_chk CHECK (expense_indtry_av_cmp_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_erycp_chk CHECK (expense_ref_year_comp_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_fresh_chk CHECK (fresh_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_fsrwl_chk CHECK (farm_size_rtios_within_lmt_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_mp_chk CHECK (margin_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_pffp_chk CHECK (production_forage_fs_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_pfvp_chk CHECK (production_fruit_veg_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_pgp_chk CHECK (production_grains_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_pp_chk CHECK (production_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_rrfvp_chk CHECK (revenue_risk_fruit_vg_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_rrgfp_chk CHECK (revenue_rsk_grain_frg_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_rrp_chk CHECK (revenue_risk_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_scp_chk CHECK (structural_change_pass_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_wdlra_chk CHECK (within_dlr_limit_ratio_add_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_wlad_chk CHECK (within_limit_addtiv_divisn_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_wlia_chk CHECK (margin_wthn_lmt_indtry_avg_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ADD CONSTRAINT farm_rtr_wlprm_chk CHECK (within_lmt_prct_ref_margin_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN reasonability_test_result_id SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN fresh_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN generated_date SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benefit_risk_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN program_year_margin SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN total_benefit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benchmark_margin_deductible SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benchmark_margin_payout_level SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benefit_benchmark_befor_combnd SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benefit_benchmark SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN benefit_risk_variance_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN margin_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN adj_reference_margin_vrinc_lmt SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN within_lmt_prct_ref_margin_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN margin_industry_average SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN margin_industry_average_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN margin_wthn_lmt_indtry_avg_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN structural_change_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN production_marg_accr_adjs SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN ratio_adj_reference_margin SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN additive_adj_reference_margin SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN ratio_additive_ref_marg_diff SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN ratio_add_diff_dollar_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN within_dlr_limit_ratio_add_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN additive_division_ratio_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN within_limit_addtiv_divisn_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN structual_change_method_to_use SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN farm_size_ratio_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN farm_size_rtios_within_lmt_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN expense_indtry_av_cmp_pass_ind SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN expense_accr_adjs_indtry_av_cm SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN expense_industry_average SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN expense_industry_average_limit SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN expense_accr_adjs SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_reasonabilty_test_results ALTER COLUMN when_created SET NOT NULL;
