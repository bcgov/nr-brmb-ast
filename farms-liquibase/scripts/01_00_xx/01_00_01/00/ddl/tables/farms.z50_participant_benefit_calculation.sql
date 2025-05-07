CREATE TABLE z50_participant_benefit_calculation(
    benefit_calculation_key               numeric(10, 0)    NOT NULL,
    participant_pin                       numeric(9, 0)     NOT NULL,
    program_year                          numeric(4, 0)     NOT NULL,
    agristability_status                  numeric(2, 0)     NOT NULL,
    unadjusted_reference_margin           numeric(13, 2),
    adjusted_reference_margin             numeric(13, 2),
    program_margin                        numeric(13, 2),
    whole_farm_indicator                  varchar(1),
    structure_change_indicator            varchar(1),
    structure_change_adjustment_amount    numeric(13, 2),
    revision_count                        numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                           varchar(30)       NOT NULL,
    create_date                           timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                           varchar(30),
    update_date                           timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z50_participant_benefit_calculation.benefit_calculation_key IS 'BENEFIT CALCULATION KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.agristability_status IS 'AGRISTABILITY STATUS is the status of this participant''s data for the current year.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.unadjusted_reference_margin IS 'UNADJUSTED REFERENCE MARGIN is initial calculated margin.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.adjusted_reference_margin IS 'ADJUSTED REFERENCE MARGIN is the final reference margin used, after adjustments for Structure change, Combining, cash vs accrual changes, and other adjustments.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.program_margin IS 'PROGRAM MARGIN is the PROGRAM YEAR margin.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.whole_farm_indicator IS 'WHOLE FARM INDICATOR indicates this participants data was combined with another participants data. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.structure_change_indicator IS 'STRUCTURE CHANGE INDICATOR indicates if this participants data was modified because of a structure change situation. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.structure_change_adjustment_amount IS 'STRUCTURE CHANGE ADJUSTMENT AMOUNT is the amount that affects the production margin of a farm (or combined whole farm) due to a structure change.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z50_participant_benefit_calculation.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z50_participant_benefit_calculation IS 'Z50 PARTICIPANT BENEFIT CALCULATION identifies benefit calculations used to determine the participants CAIS Benefits, including adjusted and unadjusted Margins, and calculation type indicators. This data can only be provided for years FIPD has processed. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpbc_pp_py ON z50_participant_benefit_calculation(participant_pin, program_year)
;
