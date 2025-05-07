CREATE TABLE z02_participant_farm_information(
    participant_pin                         numeric(9, 0)    NOT NULL,
    program_year                            numeric(4, 0)    NOT NULL,
    form_version_number                     numeric(4, 0)    NOT NULL,
    province_of_residence                   varchar(2)       NOT NULL,
    province_of_main_farmstead              varchar(2),
    postmark_date                           varchar(8)       NOT NULL,
    received_date                           varchar(8)       NOT NULL,
    sole_proprietor_indicator               varchar(1),
    partnership_member_indicator            varchar(1),
    corporate_shareholder_indicator         varchar(1),
    coop_member_indicator                   varchar(1),
    common_share_total                      numeric(8, 0),
    farm_years                              numeric(2, 0),
    last_year_farming_indicator             varchar(1),
    form_origin_code                        numeric(2, 0),
    industry_code                           numeric(6, 0),
    participant_profile_code                numeric(1, 0)    NOT NULL,
    accrual_cash_conversion_indicator       varchar(1),
    perishable_commodities_indicator        varchar(1),
    receipts_indicator                      varchar(1),
    other_text_indicator                    varchar(1),
    other_text                              varchar(100),
    accrual_worksheet_indicator             varchar(1),
    cwb_worksheet_indicator                 varchar(1),
    combined_this_year_indicator            varchar(1),
    completed_production_cycle_indicator    varchar(1),
    disaster_indicator                      varchar(1),
    copy_cob_to_contact_indicator           varchar(1),
    municipality_code                       numeric(3, 0),
    form_version_effective_date             varchar(8)       NOT NULL,
    revision_count                          numeric(5, 0)    DEFAULT 1 NOT NULL,
    create_user                             varchar(30)      NOT NULL,
    create_date                             timestamp(6)     DEFAULT systimestamp NOT NULL,
    update_user                             varchar(30),
    update_date                             timestamp(6)     DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z02_participant_farm_information.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z02_participant_farm_information.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z02_participant_farm_information.form_version_number IS 'FORM VERSION NUMBER Distinguishes between different versions of the AgriStability application from the producer. Both the producer and the administration can initiate adjustments that create a new form version in a specific PROGRAM YEAR.'
;
COMMENT ON COLUMN z02_participant_farm_information.province_of_residence IS 'PROVINCE OF RESIDENCE is the province of Residence.'
;
COMMENT ON COLUMN z02_participant_farm_information.province_of_main_farmstead IS 'PROVINCE OF MAIN FARMSTEAD is province of Main Farmstead from page 1 of T1163 E.'
;
COMMENT ON COLUMN z02_participant_farm_information.postmark_date IS 'POSTMARK DATE is the Date Form was Postmarked. Will be received date if received before filing deadline.'
;
COMMENT ON COLUMN z02_participant_farm_information.received_date IS 'RECEIVED DATE is the date form was received by RCT.'
;
COMMENT ON COLUMN z02_participant_farm_information.sole_proprietor_indicator IS 'SOLE PROPRIETOR INDICATOR is Y if carried on farming business as a sole proprietor, otherwise N. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.partnership_member_indicator IS 'PARTNERSHIP MEMBER INDICATOR is Y if carried on farming business partner of a partnership; otherwise N. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.corporate_shareholder_indicator IS 'CORPORATE SHAREHOLDER INDICATOR is Y  if carried on farming business as a shareholder of a corporation; otherwise N. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.coop_member_indicator IS 'COOP MEMBER INDICATOR is Y if carried on farming business as a member of a co-operative, otherwise N.'
;
COMMENT ON COLUMN z02_participant_farm_information.common_share_total IS 'COMMON SHARE TOTAL denoted the the outstanding common shares.'
;
COMMENT ON COLUMN z02_participant_farm_information.farm_years IS 'FARM YEARS is the number of farming years.'
;
COMMENT ON COLUMN z02_participant_farm_information.last_year_farming_indicator IS 'LAST YEAR FARMING INDICATOR was the last year of farming. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.form_origin_code IS 'FORM ORIGIN CODE indicates how the form information was received at CCRA.'
;
COMMENT ON COLUMN z02_participant_farm_information.industry_code IS 'INDUSTRY CODE is the code for the industry.'
;
COMMENT ON COLUMN z02_participant_farm_information.participant_profile_code IS 'PARTICIPANT PROFILE CODE is a code to indicate which programs the participant is applying for.'
;
COMMENT ON COLUMN z02_participant_farm_information.accrual_cash_conversion_indicator IS 'ACCRUAL CASH CONVERSION INDICATOR indicates if the accrual to cash/cash to accrual conversions box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.perishable_commodities_indicator IS 'PERISHABLE COMMODITIES INDICATOR indicates if the Perishable Commodities Worksheet box checked.  Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.receipts_indicator IS 'RECEIPTS INDICATOR indicates if the receipts box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.other_text_indicator IS 'OTHER TEXT INDICATOR identifies if Other Text ind is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.other_text IS 'OTHER TEXT contains the additional text if the other box is checked.'
;
COMMENT ON COLUMN z02_participant_farm_information.accrual_worksheet_indicator IS 'ACCRUAL WORKSHEET INDICATOR indicates if the Accrual Reference Margin Worksheet box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.cwb_worksheet_indicator IS 'CWB WORKSHEET INDICATOR indicates if the CWB Adjustment Worksheet box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.combined_this_year_indicator IS 'COMBINED THIS YEAR INDICATOR indicates if the "Should this operation be combined for" box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.completed_production_cycle_indicator IS 'COMPLETED PRODUCTION CYCLE INDICATOR indicates if the "Have you completed a production cycle on at least one of the commodities you produced?" box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.disaster_indicator IS 'DISASTER IND indicates if the "were you unable to complete a production cycle due to disaster circumstances" box is checked. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.copy_cob_to_contact_indicator IS 'COPY COB TO CONTACT INDICATOR can be "Y" or "N" - "Y" indicates that a copy of the Calculation of Benefits (COB) statement should be sent to the Contact person.  Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z02_participant_farm_information.municipality_code IS 'MUNICIPALITY CODE is the three digit code for the Municipality.'
;
COMMENT ON COLUMN z02_participant_farm_information.form_version_effective_date IS 'FORM VERSION EFFECTIVE DATE is the date the form version information was last updated.'
;
COMMENT ON COLUMN z02_participant_farm_information.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z02_participant_farm_information.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z02_participant_farm_information.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z02_participant_farm_information.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z02_participant_farm_information.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z02_participant_farm_information IS 'Z02 PARTICIPANT FARM INFORMATION lists AgriStablity Participant Farming information, from page 1/Section 1 of the Harmonized t1273 form. This file is sent to the provinces by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data'
;

