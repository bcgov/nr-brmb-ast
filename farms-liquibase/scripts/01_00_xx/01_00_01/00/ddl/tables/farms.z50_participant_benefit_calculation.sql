CREATE TABLE farms.farm_z50_participnt_bnft_calcs(
    benefit_calc_key               numeric(10, 0)    NOT NULL,
    participant_pin                       numeric(9, 0)     NOT NULL,
    program_year                          numeric(4, 0)     NOT NULL,
    agristability_status                  numeric(2, 0)     NOT NULL,
    unadjusted_reference_margin           numeric(13, 2),
    adjusted_reference_margin             numeric(13, 2),
    program_margin                        numeric(13, 2),
    whole_farm_ind                  varchar(1),
    structure_change_ind            varchar(1),
    structure_change_adj_amount    numeric(13, 2),
    revision_count                        numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                           varchar(30)       NOT NULL,
    when_created                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                           varchar(30),
    when_updated                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.benefit_calc_key IS 'BENEFIT CALCULATION KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.agristability_status IS 'AGRISTABILITY STATUS is the status of this participant''s data for the current year.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.unadjusted_reference_margin IS 'UNADJUSTED REFERENCE MARGIN is initial calculated margin.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.adjusted_reference_margin IS 'ADJUSTED REFERENCE MARGIN is the final reference margin used, after adjustments for Structure change, Combining, cash vs accrual changes, and other adjustments.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.program_margin IS 'PROGRAM MARGIN is the PROGRAM YEAR margin.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.whole_farm_ind IS 'WHOLE FARM INDICATOR indicates this participants data was combined with another participants data. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.structure_change_ind IS 'STRUCTURE CHANGE INDICATOR indicates if this participants data was modified because of a structure change situation. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.structure_change_adj_amount IS 'STRUCTURE CHANGE ADJUSTMENT AMOUNT is the amount that affects the production margin of a farm (or combined whole farm) due to a structure change.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z50_participnt_bnft_calcs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z50_participnt_bnft_calcs IS 'Z50 PARTICIPANT BENEFIT CALCULATION identifies benefit calculations used to determine the participants CAIS Benefits, including adjusted and unadjusted Margins, and calculation type indicators. This data can only be provided for years FIPD has processed. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpbc_pp_py ON farms.farm_z50_participnt_bnft_calcs(participant_pin, program_year)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z50_participnt_bnft_calcs ADD 
    CONSTRAINT pk_zpbc PRIMARY KEY (benefit_calc_key) USING INDEX TABLESPACE pg_default 
;
