CREATE TABLE scenario_enrolment(
    scenario_enrolment_id                    numeric(10, 0)    NOT NULL,
    enrolment_year                           numeric(4, 0)     NOT NULL,
    enrolment_fee                            numeric(13, 2),
    contribution_margin                      numeric(13, 2),
    benefit_calculated_indicator             varchar(1)        DEFAULT 'N' NOT NULL,
    proxy_margins_calculated_indicator       varchar(1)        DEFAULT 'N' NOT NULL,
    manual_calculated_indicator              varchar(1)        DEFAULT 'N' NOT NULL,
    has_productive_units_indicator           varchar(1)        DEFAULT 'N' NOT NULL,
    has_benchmark_per_units_indicator        varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_enrolment_fee                    numeric(13, 2),
    benefit_contribution_margin              numeric(16, 2),
    proxy_enrolment_fee                      numeric(13, 2),
    proxy_contribution_margin                numeric(16, 2),
    manual_enrolment_fee                     numeric(16, 2),
    manual_contribution_margin               numeric(16, 2),
    margin_year_minus_2                      numeric(16, 2),
    margin_year_minus_3                      numeric(16, 2),
    margin_year_minus_4                      numeric(16, 2),
    margin_year_minus_5                      numeric(16, 2),
    margin_year_minus_6                      numeric(16, 2),
    margin_year_minus_2_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_3_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_4_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_5_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_6_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_margin_year_minus_2              numeric(16, 2),
    benefit_margin_year_minus_3              numeric(16, 2),
    benefit_margin_year_minus_4              numeric(16, 2),
    benefit_margin_year_minus_5              numeric(16, 2),
    benefit_margin_year_minus_6              numeric(16, 2),
    benefit_margin_year_minus_2_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_margin_year_minus_3_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_margin_year_minus_4_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_margin_year_minus_5_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    benefit_margin_year_minus_6_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    proxy_margin_year_minus_2                numeric(16, 2),
    proxy_margin_year_minus_3                numeric(16, 2),
    proxy_margin_year_minus_4                numeric(16, 2),
    manual_margin_year_minus_2               numeric(16, 2),
    manual_margin_year_minus_3               numeric(16, 2),
    manual_margin_year_minus_4               numeric(16, 2),
    combined_farm_percent                    numeric(4, 3),
    enrolment_calculation_type_code          varchar(10)       NOT NULL,
    agristability_scenario_id                numeric(10, 0)    NOT NULL,
    revision_count                           numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                              varchar(30)       NOT NULL,
    create_date                              timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                              varchar(30),
    update_date                              timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN scenario_enrolment.scenario_enrolment_id IS 'SCENARIO ENROLMENT ID is a surrogate unique identifier for REASONABILTY TEST RESULT.'
;
COMMENT ON COLUMN scenario_enrolment.enrolment_year IS 'ENROLMENT YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN scenario_enrolment.enrolment_fee IS 'ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.'
;
COMMENT ON COLUMN scenario_enrolment.contribution_margin IS 'CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_calculated_indicator IS 'BENEFIT CALCULATED INDICATOR indicates if the benefit was calculated successfully for the AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_margins_calculated_indicator IS 'PROXY MARGINS CALCULATED INDICATOR indicates if the enrolment was calculated successfully using proxy margins.'
;
COMMENT ON COLUMN scenario_enrolment.manual_calculated_indicator IS 'MANUAL CALCULATED INDICATOR indicates if the enrolment was calculated successfully using manually calculated margins.'
;
COMMENT ON COLUMN scenario_enrolment.has_productive_units_indicator IS 'HAS PRODUCTIVE UNITS INDICATOR indicates if the AGRISTABILITY SCENARIO has PRODUCTIVE UNIT CAPACITY with non-zero amounts.'
;
COMMENT ON COLUMN scenario_enrolment.has_benchmark_per_units_indicator IS 'HAS BENCHMARK PER UNITS INDICATOR indicates if the PRODUCTIVE UNIT CAPACITY records of the AGRISTABILITY SCENARIO have BENCHMARK PER UNITs.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_enrolment_fee IS 'BENEFIT ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_contribution_margin IS 'BENEFIT CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_enrolment_fee IS 'PROXY ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_contribution_margin IS 'PROXY CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.'
;
COMMENT ON COLUMN scenario_enrolment.manual_enrolment_fee IS 'MANUAL ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.'
;
COMMENT ON COLUMN scenario_enrolment.manual_contribution_margin IS 'MANUAL CONTRIBUTION MARGIN is the calculated margin used to calculate the ENROLMENT FEE. How it is calculated depends on the CALCULATION TYPE CODE selected by the user.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_2 IS 'MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_3 IS 'MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_4 IS 'MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_5 IS 'MARGIN YEAR MINUS 5 is the Margin from the ENROLMENT YEAR minus 5 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_6 IS 'MARGIN YEAR MINUS 6 is the Margin from the ENROLMENT YEAR minus 6 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_2_indicator IS 'MARGIN YEAR MINUS 2 INDICATOR identifies if the MARGIN YEAR MINUS 2 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_3_indicator IS 'MARGIN YEAR MINUS 3 IND identifies if the MARGIN YEAR MINUS 3 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_4_indicator IS 'MARGIN YEAR MINUS 4 INDICATOR identifies if the MARGIN YEAR MINUS 4 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_5_indicator IS 'MARGIN YEAR MINUS 5 INDICATOR identifies if the MARGIN YEAR MINUS 5 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.margin_year_minus_6_indicator IS 'MARGIN YEAR MINUS 6 INDICATOR identifies if the MARGIN YEAR MINUS 6 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_2 IS 'BENEFIT MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_3 IS 'BENEFIT MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_4 IS 'BENEFIT MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_5 IS 'BENEFIT MARGIN YEAR MINUS 5 is the Margin from the ENROLMENT YEAR minus 5 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_6 IS 'BENEFIT MARGIN YEAR MINUS 6 is the Margin from the ENROLMENT YEAR minus 6 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_2_indicator IS 'BENEFIT MARGIN YEAR MINUS 2 INDICATOR identifies if the BENEFIT MARGIN YEAR MINUS 2 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_3_indicator IS 'BENEFIT MARGIN YEAR MINUS 3 INDICATOR identifies if the BENEFIT MARGIN YEAR MINUS 3 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_4_indicator IS 'BENEFIT MARGIN YEAR MINUS 4 INDICATOR identifies if the BENEFIT MARGIN YEAR MINUS 4 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_5_indicator IS 'BENEFIT MARGIN YEAR MINUS 5 INDICATOR identifies if the BENEFIT MARGIN YEAR MINUS 5 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.benefit_margin_year_minus_6_indicator IS 'BENEFIT MARGIN YEAR MINUS 6 INDICATOR identifies if the BENEFIT MARGIN YEAR MINUS 6 was used to calculate the CONTRIBUTION MARGIN.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_margin_year_minus_2 IS 'PROXY MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_margin_year_minus_3 IS 'PROXY MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.proxy_margin_year_minus_4 IS 'PROXY MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.manual_margin_year_minus_2 IS 'MANUAL MARGIN YEAR MINUS 2 is the Margin from the ENROLMENT YEAR minus 2 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.manual_margin_year_minus_3 IS 'MANUAL MARGIN YEAR MINUS 3 is the Margin from the ENROLMENT YEAR minus 3 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.manual_margin_year_minus_4 IS 'MANUAL MARGIN YEAR MINUS 4 is the Margin from the ENROLMENT YEAR minus 4 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.combined_farm_percent IS 'COMBINED FARM PERCENT applies only to farms that are part of a combined farm. This is the fraction of the ENROLMENT FEE and margins (CONTRIBUTION MARGIN, MARGIN YEAR MINUS 2 to 6) for this AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN scenario_enrolment.enrolment_calculation_type_code IS 'ENROLMENT CALCULATION TYPE CODE is a unique code for the object ENROLMENT CALCULATION TYPE CODE. Examples of codes and descriptions are BENEFIT - Benefit, PROXY - Proxy, MANUAL - Manual.'
;
COMMENT ON COLUMN scenario_enrolment.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_enrolment.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN scenario_enrolment.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN scenario_enrolment.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN scenario_enrolment.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN scenario_enrolment.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE scenario_enrolment IS 'SCENARIO ENROLMENT contains information pertaining to an AGRISTABILITY CLIENT''''s enrolment in the AgriStaibility program. The calculation uses the associated AGRISTABILITY SCENARIO to calculate the enrolment fee. Only for scenarios with SCENARIO CATEGORY CODE "ENW".'
;

