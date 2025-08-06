CREATE TABLE farms.farm_zprogram_enrolments(
    participant_pin                  numeric(9, 0)     NOT NULL,
    enrolment_year                   numeric(4, 0)     NOT NULL,
    enrolment_fee                    numeric(13, 2),
    failed_to_generate_ind     varchar(1)        DEFAULT 'N' NOT NULL,
    error_ind                  varchar(1)        DEFAULT 'N' NOT NULL,
    generated_from_cra_ind     varchar(1)        DEFAULT 'N' NOT NULL,
    generated_from_enw_ind     varchar(1)        DEFAULT 'N' NOT NULL,
    failed_reason                    varchar(1000),
    generated_date                   date,
    contribution_margin_average      numeric(16, 2),
    margin_year_minus_2              numeric(16, 2),
    margin_year_minus_3              numeric(16, 2),
    margin_year_minus_4              numeric(16, 2),
    margin_year_minus_5              numeric(16, 2),
    margin_year_minus_6              numeric(16, 2),
    margin_year_minus_2_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_3_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_4_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_5_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    margin_year_minus_6_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    create_task_in_barn_ind    varchar(1)        DEFAULT 'N' NOT NULL,
    combined_farm_percent            numeric(4, 3),
    agristability_scenario_id        numeric(10, 0),
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                      varchar(30)       NOT NULL,
    when_created                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                      varchar(30),
    when_updated                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_zprogram_enrolments.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.enrolment_year IS 'ENROLMENT YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.enrolment_fee IS 'ENROLMENT FEE is the amount the AGRISTABILITY CLIENT must pay to be enrolled in the ArgiStability program for a given year.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.failed_to_generate_ind IS 'FAILED TO GENERATE INDICATOR identifies if the "Enrolment Notice" could not be generated for the AGRISTABILITY CLIENT. Currently the only reason why an "Enrolment Notice" might fail to be generated is due to a lack of CRA data from previous years.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.error_ind IS 'ERROR INDICATOR identifies if an error occurred while generating the "Enrolment Notice" for the AGRISTABILITY CLIENT. If FAILED TO GENERATE INDICATOR is set to Y and ERROR INDICATOR is set to N then there was warning.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_from_cra_ind IS 'GENERATED FROM CRA INDICATOR identifies if the margins and enrolment fee were generated from a CRA scenario because margins from a USER scenario were not available.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_from_enw_ind IS 'GENERATED FROM ENW INDICATOR identifies if the margins and enrolment fee were generated from a scenario with SCENARIO CATEGORY CODE ENW (Enrolment Notice Workflow).'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.failed_reason IS 'FAILED REASON identifies the reason why the "Enrolment Notice" could not be generated for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.generated_date IS 'GENERATED DATE identifies the date that the AGRISTATBILITY CLIENT''s yearly "Enrolment Notice" letter was generated.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.contribution_margin_average IS 'CONTRIBUTION MARGIN AVERAGE is the olympic average of the Program Year Margins for Program Year minus 2 to Program Year minus 6 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_2 IS 'MARGIN YEAR MINUS 2 is the Program Year Margin from the Program Year minus 2 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_3 IS 'MARGIN YEAR MINUS 3 is the Program Year Margin from the Program Year minus 3 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_4 IS 'MARGIN YEAR MINUS 4 is the Program Year Margin from the Program Year minus 4 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_5 IS 'MARGIN YEAR MINUS 5 is the Program Year Margin from the Program Year minus 5 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_6 IS 'MARGIN YEAR MINUS 6 is the Program Year Margin from the Program Year minus 6 for the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_2_ind IS 'MARGIN YEAR MINUS 2 INDICATOR identifies if the Program Year Margin from the Program Year minus 2 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_3_ind IS 'MARGIN YEAR MINUS 3 INDICATOR identifies if the Program Year Margin from the Program Year minus 3 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_4_ind IS 'MARGIN YEAR MINUS 4 INDICATOR identifies if the Program Year Margin from the Program Year minus 4 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_5_ind IS 'MARGIN YEAR MINUS 5 INDICATOR identifies if the Program Year Margin from the Program Year minus 5 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.margin_year_minus_6_ind IS 'MARGIN YEAR MINUS 6 INDICATOR identifies if the Program Year Margin from the Program Year minus 6 was used to calculate the CONTRIBUTION MARGIN. It would not be used if there was no value or if it was the lowest or highest value in the olympic average.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.create_task_in_barn_ind IS 'CREATE TASK IN BARN INDICATOR identifies if a workflow task should be created in the BARNS system.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.combined_farm_percent IS 'COMBINED FARM PERCENT applies only to farms that are part of a combined farm. This is the fraction of the ENROLMENT FEE and margins (CONTRIBUTION MARGIN, MARGIN YEAR MINUS 2 to 6) for this AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO. It identifies the scenario that the program year margins were retrieved from.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zprogram_enrolments.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_zprogram_enrolments IS 'ZPROGRAM ENROLMENT is used to temporarily store information pertaining to an AGRISTABILITY CLIENT''''s enrolment in the AgriStaibility program, until it is ready to be copied to the operational table: PROGRAM ENROLMENT.'
;


ALTER TABLE farms.farm_zprogram_enrolments ADD 
    CONSTRAINT pk_ze PRIMARY KEY (participant_pin) USING INDEX TABLESPACE pg_default 
;
