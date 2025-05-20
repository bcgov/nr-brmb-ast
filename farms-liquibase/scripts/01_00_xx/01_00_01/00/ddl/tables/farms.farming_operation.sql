CREATE TABLE farms.farming_operation(
    farming_operation_id             numeric(10, 0)    NOT NULL,
    business_use_home_expense        numeric(13, 2),
    expenses                         numeric(13, 2),
    fiscal_year_end                  date,
    fiscal_year_start                date,
    gross_income                     numeric(13, 2),
    inventory_adjustments            numeric(13, 2),
    crop_share_indicator             varchar(1)        NOT NULL,
    feeder_member_indicator          varchar(1)        NOT NULL,
    landlord_indicator               varchar(1)        NOT NULL,
    net_farm_income                  numeric(13, 2),
    net_income_after_adjustments     numeric(13, 2),
    net_income_before_adjustments    numeric(13, 2),
    other_deductions                 numeric(13, 2),
    partnership_name                 varchar(42),
    partnership_percent              numeric(7, 4)     NOT NULL,
    partnership_pin                  numeric(9, 0)     NOT NULL,
    operation_number                 numeric(4, 0)     NOT NULL,
    crop_disaster_indicator          varchar(1),
    livestock_disaster_indicator     varchar(1),
    locally_updated_indicator        varchar(1)        DEFAULT 'N' NOT NULL,
    locally_generated_indicator      varchar(1)        DEFAULT 'N' NOT NULL,
    alignment_key                    varchar(2)        NOT NULL,
    federal_accounting_code          varchar(10),
    program_year_version_id          numeric(10, 0)    NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farming_operation.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN farms.farming_operation.business_use_home_expense IS 'BUSINESS USE HOME EXPENSE is the allowable portion of Business-use-of-home expenses (9934).'
;
COMMENT ON COLUMN farms.farming_operation.expenses IS 'EXPENSES are the Total Expenses for the FARMING OPERATION (9968).'
;
COMMENT ON COLUMN farms.farming_operation.fiscal_year_end IS 'FISCAL YEAR END is the Operation at the end of the Fiscal Year  (yyyymmdd - may be blank).'
;
COMMENT ON COLUMN farms.farming_operation.fiscal_year_start IS 'FISCAL YEAR START is the Operation at the start of the Fiscal year  (yyyymmdd - may be blank).'
;
COMMENT ON COLUMN farms.farming_operation.gross_income IS 'GROSS INCOME is the income before taxes or deductions for the FARMING OPERATION. Uses form field number (9959) .'
;
COMMENT ON COLUMN farms.farming_operation.inventory_adjustments IS 'INVENTORY ADJUSTMENTS  are the changes to reported inventory  quantities. Curr Year (9941 and 9942).'
;
COMMENT ON COLUMN farms.farming_operation.crop_share_indicator IS 'CROP SHARE INDICATOR denotes if this participant participated in a crop share as a tenant.'
;
COMMENT ON COLUMN farms.farming_operation.feeder_member_indicator IS 'FEEDER MEMBER INDICATOR denotes if this participant was a member of a feeder association.'
;
COMMENT ON COLUMN farms.farming_operation.landlord_indicator IS 'LANDLORD INDICATOR denotes if this Participant carried on a farming business as a landlord.'
;
COMMENT ON COLUMN farms.farming_operation.net_farm_income IS 'NET FARM INCOME denotes the Net Farming Income after adjustments (9946).'
;
COMMENT ON COLUMN farms.farming_operation.net_income_after_adjustments IS 'NET INCOME AFTER ADJUSTMENTS is the Net Income after adjustments amount (line 9944).'
;
COMMENT ON COLUMN farms.farming_operation.net_income_before_adjustments IS 'NET INCOME BEFORE ADJUSTMENTS is the Net Income before adjustment (9969).'
;
COMMENT ON COLUMN farms.farming_operation.other_deductions IS 'OTHER DEDUCTIONS to the net income amount (line 9940).'
;
COMMENT ON COLUMN farms.farming_operation.partnership_name IS 'PARTNERSHIP NAME is the name of the partnership for the FARMING OPERATION.'
;
COMMENT ON COLUMN farms.farming_operation.partnership_percent IS 'PARTNERSHIP PERCENT is the Percentage of the  Partnership (100% will be stored as 1.0).'
;
COMMENT ON COLUMN farms.farming_operation.partnership_pin IS 'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. Partnership pins represent the same operation if/when they are used in different stab years.'
;
COMMENT ON COLUMN farms.farming_operation.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given year. Operations may have different OPERATION NUMBER in different years.'
;
COMMENT ON COLUMN farms.farming_operation.crop_disaster_indicator IS 'CROP DISASTER INDICATOR identifies if the productive capacity decreased due to disaster circumstances.'
;
COMMENT ON COLUMN farms.farming_operation.livestock_disaster_indicator IS 'LIVESTOCK DISASTER INDICATOR identifies if productive capacity decreased due to disaster circumstances.'
;
COMMENT ON COLUMN farms.farming_operation.locally_updated_indicator IS 'LOCALLY UPDATED INDICATOR identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the FARMING OPERATION information for a given year; as a result, the FARM system will not allow the overwriting of that particular FARMING OPERATION data by subsequent imports for the same year.'
;
COMMENT ON COLUMN farms.farming_operation.locally_generated_indicator IS 'LOCALLY GENERATED INDICATOR identifies if the record was created by a user. If this value is ''Y'', the client created this FARMING OPERATION information for a given year.'
;
COMMENT ON COLUMN farms.farming_operation.alignment_key IS 'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.'
;
COMMENT ON COLUMN farms.farming_operation.federal_accounting_code IS 'FEDERAL ACCOUNTING CODE is a unique code for the object FEDERAL ACCOUNTING CODE described as a numeric code used to uniquely identify the method of Accounting to RCT and NISA.  Examples of codes and descriptions are 1- Accrual, 2-Cash.'
;
COMMENT ON COLUMN farms.farming_operation.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON COLUMN farms.farming_operation.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farming_operation.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farming_operation.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farming_operation.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farming_operation.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farming_operation IS 'FARMING OPERATION refers to revenue and expense information for an AGRISTABILITY CLIENT- i.e. corresponding to tax return statements. An AGRISTABILITY CLIENT can have multiple instances of a FARMING OPERATION in a given Program Year. FARMING OPERATION data will originate from federal data imports. Updates to FARMING OPERATION data may be received for past years. FARMING OPERATION data will only exist from 2003 forward (i.e. the origin of the CAIS/AgriStability program).'
;


CREATE INDEX ix_fo_fac ON farms.farming_operation(federal_accounting_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_fo_pyvi ON farms.farming_operation(program_year_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_fo_foi_pyvi ON farms.farming_operation(farming_operation_id, program_year_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_fo_foi_on_pyvi ON farms.farming_operation(farming_operation_id, operation_number, program_year_version_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_fo_pyvi_on ON farms.farming_operation(program_year_version_id, operation_number)
 TABLESPACE pg_default
;

ALTER TABLE farms.farming_operation ADD 
    CONSTRAINT pk_fo PRIMARY KEY (farming_operation_id) USING INDEX TABLESPACE pg_default 
;
