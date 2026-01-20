CREATE TABLE farms.farm_zprogram_enrolments (
	participant_pin integer NOT NULL,
	enrolment_year smallint NOT NULL,
	enrolment_fee decimal(13,2),
	failed_to_generate_ind varchar(1) NOT NULL DEFAULT 'N',
	error_ind varchar(1) NOT NULL DEFAULT 'N',
	generated_from_cra_ind varchar(1) NOT NULL DEFAULT 'N',
	generated_from_enw_ind varchar(1) NOT NULL DEFAULT 'N',
	failed_reason varchar(1000),
	generated_date timestamp(0),
	contribution_margin_average decimal(16,2),
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
	create_task_in_barn_ind varchar(1) NOT NULL DEFAULT 'N',
	combined_farm_percent decimal(4,3),
	agristability_scenario_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_zprogram_enrolments IS E'ZPROGRAM ENROLMENT is used to temporarily store information pertaining to an AGRISTABILITY CLIENT''s enrolment in the AgriStaibility program, until it is ready to be copied to the operational table: PROGRAM ENROLMENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO. It identifies the scenario that the program year margins were retrieved from.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.combined_farm_percent IS E'COMBINED FARM PERCENT applies only to farms that are part of a combined farm. This is the fraction of the ENROLMENT FEE and margins (CONTRIBUTION MARGIN, MARGIN YEAR MINUS 2 to 6) for this AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.contribution_margin_average IS E'CONTRIBUTION MARGIN AVERAGE is the olympic average of the Program Year Margins for Program Year minus 2 to Program Year minus 6 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.create_task_in_barn_ind IS E'CREATE TASK IN BARN IND identifies if a workflow task should be created in the BARNS system.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.enrolment_fee IS E'ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.enrolment_year IS E'ENROLMENT YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.error_ind IS E'ERROR IND identifies if an error occurred while generating the "Enrolment Notice" for the AGRISTABILITY CLIENT. If FAILED TO GENERATE IND is set to Y and ERROR IND is set to N then there was warning.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.failed_reason IS E'FAILED REASON identifies the reason why the "Enrolment Notice" could not be generated for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.failed_to_generate_ind IS E'FAILED TO GENERATE IND identifies if the "Enrolment Notice" could not be generated for the AGRISTABILITY CLIENT. Currently the only reason why an "Enrolment Notice" might fail to be generated is due to a lack of CRA data from previous years.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_date IS E'GENERATED DATE identifies the date that the AGRISTATBILITY CLIENT''s yearly "Enrolment Notice" letter was generated.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_from_cra_ind IS E'GENERATED FROM CRA IND identifies if the margins and enrolment fee were generated from a CRA scenario because margins from a USER scenario were not available.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_from_enw_ind IS E'GENERATED FROM ENW IND identifies if the margins and enrolment fee were generated from a scenario with SCENARIO CATEGORY CODE ENW (Enrolment Notice Workflow).';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_2 IS E'MARGIN YEAR MINUS 2 is the Program Year Margin from the Program Year minus 2 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_2_ind IS E'MARGIN YEAR MINUS 2 IND identifies if the Program Year Margin from the Program Year minus 2 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_3 IS E'MARGIN YEAR MINUS 3 is the Program Year Margin from the Program Year minus 3 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_3_ind IS E'MARGIN YEAR MINUS 3 IND identifies if the Program Year Margin from the Program Year minus 3 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_4 IS E'MARGIN YEAR MINUS 4 is the Program Year Margin from the Program Year minus 4 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_4_ind IS E'MARGIN YEAR MINUS 4 IND identifies if the Program Year Margin from the Program Year minus 4 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_5 IS E'MARGIN YEAR MINUS 5 is the Program Year Margin from the Program Year minus 5 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_5_ind IS E'MARGIN YEAR MINUS 5 IND identifies if the Program Year Margin from the Program Year minus 5 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_6 IS E'MARGIN YEAR MINUS 6 is the Program Year Margin from the Program Year minus 6 for the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_6_ind IS E'MARGIN YEAR MINUS 6 IND identifies if the Program Year Margin from the Program Year minus 6 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_zprogram_enrolments.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_pk PRIMARY KEY (participant_pin);
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_error_chk CHECK (error_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_failed_to_gen_chk CHECK (failed_to_generate_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_gen_from_cra_chk CHECK (generated_from_cra_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_gen_from_enw_chk CHECK (generated_from_enw_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_margin_yr_minus_2_chk CHECK (margin_year_minus_2_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_margin_yr_minus_3_chk CHECK (margin_year_minus_3_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_margin_yr_minus_4_chk CHECK (margin_year_minus_4_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_margin_yr_minus_5_chk CHECK (margin_year_minus_5_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_margin_yr_minus_6_chk CHECK (margin_year_minus_6_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ADD CONSTRAINT farm_zpe_task_chk CHECK (create_task_in_barn_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN enrolment_year SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN failed_to_generate_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN error_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN generated_from_cra_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN generated_from_enw_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN margin_year_minus_2_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN margin_year_minus_3_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN margin_year_minus_4_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN margin_year_minus_5_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN margin_year_minus_6_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN create_task_in_barn_ind SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_zprogram_enrolments ALTER COLUMN when_created SET NOT NULL;
