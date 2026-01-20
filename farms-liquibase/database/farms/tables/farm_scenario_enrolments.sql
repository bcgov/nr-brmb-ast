CREATE TABLE farms.farm_scenario_enrolments (
	scenario_enrolment_id bigint NOT NULL,
	enrolment_year smallint NOT NULL,
	enrolment_fee decimal(13,2),
	contribution_margin decimal(13,2),
	benefit_calculated_ind varchar(1) NOT NULL DEFAULT 'N',
	proxy_margins_calculated_ind varchar(1) NOT NULL DEFAULT 'N',
	manual_calculated_ind varchar(1) NOT NULL DEFAULT 'N',
	has_productive_units_ind varchar(1) NOT NULL DEFAULT 'N',
	has_benchmark_per_units_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_enrolment_fee decimal(13,2),
	benefit_contribution_margin decimal(16,2),
	proxy_enrolment_fee decimal(13,2),
	proxy_contribution_margin decimal(16,2),
	manual_enrolment_fee decimal(16,2),
	manual_contribution_margin decimal(16,2),
	margin_year_minus_2 decimal(16,2),
	margin_year_minus_3 decimal(16,2),
	margin_year_minus_4 decimal(16,2),
	margin_year_minus_5 decimal(16,2),
	margin_year_minus_6 decimal(16,2),
	margin_year_minus_2_ind varchar(1) NOT NULL DEFAULT 'N',
	margin_year_minus_3_ind varchar(1) NOT NULL DEFAULT 'N',
	margin_year_minus_4_ind varchar(1) NOT NULL DEFAULT 'N',
	margin_year_minus_5_ind varchar(1) NOT NULL DEFAULT 'N',
	margin_year_minus_6_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_margin_year_minus_2 decimal(16,2),
	benefit_margin_year_minus_3 decimal(16,2),
	benefit_margin_year_minus_4 decimal(16,2),
	benefit_margin_year_minus_5 decimal(16,2),
	benefit_margin_year_minus_6 decimal(16,2),
	benefit_margn_year_minus_2_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_margn_year_minus_3_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_margn_year_minus_4_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_margn_year_minus_5_ind varchar(1) NOT NULL DEFAULT 'N',
	benefit_margn_year_minus_6_ind varchar(1) NOT NULL DEFAULT 'N',
	proxy_margin_year_minus_2 decimal(16,2),
	proxy_margin_year_minus_3 decimal(16,2),
	proxy_margin_year_minus_4 decimal(16,2),
	manual_margin_year_minus_2 decimal(16,2),
	manual_margin_year_minus_3 decimal(16,2),
	manual_margin_year_minus_4 decimal(16,2),
	combined_farm_percent decimal(4,3),
	enrolment_calc_type_code varchar(10) NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_scenario_enrolments IS E'SCENARIO ENROLMENT contains information pertaining to an AGRISTABILITY CLIENT''s enrolment in the AgriStaibility program. The calculation uses the associated AGRISTABILITY SCENARIO to calculate the enrolment fee. Only for scenarios with SCENARIO CATEGORY CODE "ENW".';
COMMENT ON COLUMN farms.farm_scenario_enrolments.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_calculated_ind IS E'BENEFIT CALCULATED IND indicates if the benefit was calculated successfully for the AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_contribution_margin IS E'BENEFIT CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_enrolment_fee IS E'BENEFIT ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margin_year_minus_2 IS E'BENEFIT MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margin_year_minus_3 IS E'BENEFIT MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margin_year_minus_4 IS E'BENEFIT MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margin_year_minus_5 IS E'BENEFIT MARGIN YEAR MINUS 5 is the Margin from the ENROLMENT YEAR minus 5 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margin_year_minus_6 IS E'BENEFIT MARGIN YEAR MINUS 6 is the Margin from the ENROLMENT YEAR minus 6 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margn_year_minus_2_ind IS E'BENEFIT MARGN YEAR MINUS 2 IND identifies if the BENEFIT MARGIN YEAR MINUS 2 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margn_year_minus_3_ind IS E'BENEFIT MARGN YEAR MINUS 3 IND identifies if the BENEFIT MARGIN YEAR MINUS 3 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margn_year_minus_4_ind IS E'BENEFIT MARGN YEAR MINUS 4 IND identifies if the BENEFIT MARGIN YEAR MINUS 4 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margn_year_minus_5_ind IS E'BENEFIT MARGN YEAR MINUS 5 IND identifies if the BENEFIT MARGIN YEAR MINUS 5 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.benefit_margn_year_minus_6_ind IS E'BENEFIT MARGN YEAR MINUS 6 IND identifies if the BENEFIT MARGIN YEAR MINUS 6 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.combined_farm_percent IS E'COMBINED FARM PERCENT applies only to farms that are part of a combined farm. This is the fraction of the ENROLMENT FEE and margins (CONTRIBUTION MARGIN, MARGIN YEAR MINUS 2 to 6) for this AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.contribution_margin IS E'CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.enrolment_calc_type_code IS E'ENROLMENT CALC TYPE CODE is a unique code for the object ENROLMENT CALC TYPE CODE. Examples of codes and descriptions are BENEFIT - Benefit, PROXY - Proxy, MANUAL - Manual.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.enrolment_fee IS E'ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.enrolment_year IS E'ENROLMENT YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.has_benchmark_per_units_ind IS E'HAS BENCHMARK PER UNITS IND indicates if the PRODUCTIVE UNIT CAPACITY records of the AGRISTABILITY SCENARIO have BENCHMARK PER UNITs.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.has_productive_units_ind IS E'HAS PRODUCTIVE UNITS IND indicates if the AGRISTABILITY SCENARIO has PRODUCTIVE UNIT CAPACITY with non-zero amounts.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_calculated_ind IS E'MANUAL CALCULATED IND indicates if the enrolment was calculated successfully using manually calculated margins.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_contribution_margin IS E'MANUAL CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_enrolment_fee IS E'MANUAL ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_margin_year_minus_2 IS E'MANUAL MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_margin_year_minus_3 IS E'MANUAL MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.manual_margin_year_minus_4 IS E'MANUAL MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_2 IS E'MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_2_ind IS E'MARGIN YEAR MINUS 2 IND identifies if the MARGIN YEAR MINUS 2 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_3 IS E'MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_3_ind IS E'MARGIN YEAR MINUS 3 IND identifies if the MARGIN YEAR MINUS 3 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_4 IS E'MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_4_ind IS E'MARGIN YEAR MINUS 4 IND identifies if the MARGIN YEAR MINUS 4 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_5 IS E'MARGIN YEAR MINUS 5 is the Margin from the ENROLMENT YEAR minus 5 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_5_ind IS E'MARGIN YEAR MINUS 5 IND identifies if the MARGIN YEAR MINUS 5 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_6 IS E'MARGIN YEAR MINUS 6 is the Margin from the ENROLMENT YEAR minus 6 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.margin_year_minus_6_ind IS E'MARGIN YEAR MINUS 6 IND identifies if the MARGIN YEAR MINUS 6 was used to calculate the CONTRIBUTION MARGIN.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_contribution_margin IS E'PROXY CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_enrolment_fee IS E'PROXY ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_margin_year_minus_2 IS E'PROXY MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_margin_year_minus_3 IS E'PROXY MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_margin_year_minus_4 IS E'PROXY MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.proxy_margins_calculated_ind IS E'PROXY MARGINS CALCULATED IND indicates if the enrolment was calculated successfully using proxy margins.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.scenario_enrolment_id IS E'SCENARIO ENROLMENT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_scenario_enrolments.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_pk PRIMARY KEY (scenario_enrolment_id);
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_uk UNIQUE (agristability_scenario_id);
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_benefit_calculated_chk CHECK (benefit_calculated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_ben_mgn_yr_minus_2_chk CHECK (benefit_margn_year_minus_2_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_ben_mgn_yr_minus_3_chk CHECK (benefit_margn_year_minus_3_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_ben_mgn_yr_minus_4_chk CHECK (benefit_margn_year_minus_4_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_ben_mgn_yr_minus_5_chk CHECK (benefit_margn_year_minus_5_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_ben_mgn_yr_minus_6_chk CHECK (benefit_margn_year_minus_6_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_has_bpus_ind_chk CHECK (has_benchmark_per_units_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_has_prod_units_ind_chk CHECK (has_productive_units_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_manual_calcltd_ind_chk CHECK (manual_calculated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_margin_yr_minus_2_chk CHECK (margin_year_minus_2_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_margin_yr_minus_3_chk CHECK (margin_year_minus_3_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_margin_yr_minus_4_chk CHECK (margin_year_minus_4_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_margin_yr_minus_5_chk CHECK (margin_year_minus_5_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_margin_yr_minus_6_chk CHECK (margin_year_minus_6_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ADD CONSTRAINT farm_se_proxy_mrgn_clc_ind_chk CHECK (proxy_margins_calculated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN scenario_enrolment_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN enrolment_year SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_calculated_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN proxy_margins_calculated_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN manual_calculated_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN has_productive_units_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN has_benchmark_per_units_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN margin_year_minus_2_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN margin_year_minus_3_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN margin_year_minus_4_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN margin_year_minus_5_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN margin_year_minus_6_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_margn_year_minus_2_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_margn_year_minus_3_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_margn_year_minus_4_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_margn_year_minus_5_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN benefit_margn_year_minus_6_ind SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN enrolment_calc_type_code SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_scenario_enrolments ALTER COLUMN when_created SET NOT NULL;
