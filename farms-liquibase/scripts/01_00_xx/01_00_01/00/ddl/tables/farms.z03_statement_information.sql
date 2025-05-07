CREATE TABLE z03_statement_information(
    operation_number                 numeric(4, 0)     NOT NULL,
    participant_pin                  numeric(9, 0)     NOT NULL,
    program_year                     numeric(4, 0)     NOT NULL,
    partnership_pin                  numeric(9, 0)     NOT NULL,
    partnership_name                 varchar(42),
    partnership_percent              numeric(6, 4)     NOT NULL,
    fiscal_year_start                varchar(8),
    fiscal_year_end                  varchar(8),
    accounting_code                  numeric(1, 0),
    landlord_indicator               varchar(1),
    crop_share_indicator             varchar(1),
    feeder_member_indicator          varchar(1),
    gross_income                     numeric(13, 2),
    expenses                         numeric(13, 2),
    net_income_before_adjustments    numeric(13, 2),
    other_deductions                 numeric(13, 2),
    inventory_adjustments            numeric(13, 2),
    net_income_after_adjustments     numeric(13, 2),
    business_use_of_home_expenses    numeric(13, 2),
    net_farm_income                  numeric(13, 2),
    crop_disaster_indicator          varchar(1),
    livestock_disaster_indicator     varchar(1),
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z03_statement_information.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN z03_statement_information.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z03_statement_information.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z03_statement_information.partnership_pin IS 'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. PARTNERSHIP PIN will  represent the same operation if/when they are used in different program years.'
;
COMMENT ON COLUMN z03_statement_information.partnership_name IS 'PARTNERSHIP NAME is the name of the partnership.'
;
COMMENT ON COLUMN z03_statement_information.partnership_percent IS 'PARTNERSHIP PERCENT is the percentage of the partnership. (100% will be stored as 1.0).'
;
COMMENT ON COLUMN z03_statement_information.fiscal_year_start IS 'FISCAL YEAR START is the Operation Fiscal year Start (yyyymmdd - may be blank)'
;
COMMENT ON COLUMN z03_statement_information.fiscal_year_end IS 'FISCAL YEAR END is the Operation FISCAL YEAR END (yyyymmdd - may be blank).'
;
COMMENT ON COLUMN z03_statement_information.accounting_code IS 'ACCOUNTING CODE is the Method of Accounting to CRA and AAFC.'
;
COMMENT ON COLUMN z03_statement_information.landlord_indicator IS 'LANDLORD INDICATOR indicates if this Participant participated in a crop share as a landlord. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z03_statement_information.crop_share_indicator IS 'CROP SHARE INDICATOR indicates if this Participant participated in a crop share as a tenant. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z03_statement_information.feeder_member_indicator IS 'FEEDER MEMBER INDICATOR indicates if this participant was a member of a feeder association. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z03_statement_information.gross_income IS 'GROSS INCOME is the GROSS INCOME (9959).'
;
COMMENT ON COLUMN z03_statement_information.expenses IS 'EXPENSES is the Total Farming Expenses (9968).'
;
COMMENT ON COLUMN z03_statement_information.net_income_before_adjustments IS 'NET INCOME BEFORE ADJUSTMENTS is the Net Income before Adjustments (9969).'
;
COMMENT ON COLUMN z03_statement_information.other_deductions IS 'OTHER DEDUCTIONS is the OTHER DEDUCTIONS to the net income amount. (9940).'
;
COMMENT ON COLUMN z03_statement_information.inventory_adjustments IS 'INVENTORY ADJUSTMENTS is the INVENTORY ADJUSTMENTS for the current year(9941 and 9942).'
;
COMMENT ON COLUMN z03_statement_information.net_income_after_adjustments IS 'NET INCOME AFTER ADJUSTMENTS is the NET INCOME AFTER ADJUSTMENTS amount (9944).'
;
COMMENT ON COLUMN z03_statement_information.business_use_of_home_expenses IS 'BUSINESS USE OF HOME EXPENSES is the allowable portion of business-use-of-home expenses (9934).'
;
COMMENT ON COLUMN z03_statement_information.net_farm_income IS 'NET FARM INCOME is Net Farming Income after adjustments (9946).'
;
COMMENT ON COLUMN z03_statement_information.crop_disaster_indicator IS 'CROP DISASTER INDICATOR is the productive capacity decreased due to disaster circumstances indicator (from page 7 of the t1273). Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z03_statement_information.livestock_disaster_indicator IS 'LIVESTOCK DISASTER INDICATOR is the productive capacity decreased due to disaster circumstances indicator (from page 7 of the t1273). Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN z03_statement_information.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z03_statement_information.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z03_statement_information.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z03_statement_information.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z03_statement_information.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z03_statement_information IS 'Z03 STATEMENT INFO identifies the data from section 3 of the Statement A. Also included are the amounts from Section 6 - Summary of Income and Expense. There will be 1 row in this file per participant, per statement A/B filled out. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zsi_pp_py ON z03_statement_information(participant_pin, program_year)
 TABLESPACE pg_default
;
