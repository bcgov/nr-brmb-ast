CREATE TABLE z04_income_expenses_detail(
    income_expense_key          numeric(10, 0)    NOT NULL,
    participant_pin             numeric(9, 0)     NOT NULL,
    program_year                numeric(4, 0)     NOT NULL,
    operation_number            numeric(4, 0)     NOT NULL,
    program_year_codified_by    numeric(4, 0)     NOT NULL,
    line_code                   numeric(4, 0)     NOT NULL,
    ie_indicator                varchar(1)        NOT NULL,
    amount                      numeric(13, 2)    NOT NULL,
    revision_count              numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                 varchar(30)       NOT NULL,
    create_date                 timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                 varchar(30),
    update_date                 timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z04_income_expenses_detail.income_expense_key IS 'INCOME EXPENSE KEY is th Primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN z04_income_expenses_detail.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z04_income_expenses_detail.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z04_income_expenses_detail.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN z04_income_expenses_detail.program_year_codified_by IS 'PROGRAM YEAR CODIFIED BY is the PROGRAM YEAR for this record.'
;
COMMENT ON COLUMN z04_income_expenses_detail.line_code IS 'LINE CODE is the Income/Expense code.'
;
COMMENT ON COLUMN z04_income_expenses_detail.ie_indicator IS 'IE INDICATOR is the Income / Expense indicator. Allowable values are I - Income, E - Expense.'
;
COMMENT ON COLUMN z04_income_expenses_detail.amount IS 'AMOUNT is the Income / Expense Amount (not adjusted for partnership percent).'
;
COMMENT ON COLUMN z04_income_expenses_detail.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z04_income_expenses_detail.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z04_income_expenses_detail.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z04_income_expenses_detail.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z04_income_expenses_detail.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z04_income_expenses_detail IS 'Z04 INCOME EXPENSES LIST identifies all the income(sales) and expense(purchases) listed by the producer on each statement A/B form. There can be multiple rows for a single income or expense code, if this is how the producer filled out the form. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;

