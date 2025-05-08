CREATE TABLE line_item(
    line_item_id                                  numeric(10, 0)    NOT NULL,
    program_year                                  numeric(4, 0)     NOT NULL,
    line_item                                     numeric(4, 0)     NOT NULL,
    description                                   varchar(256)      NOT NULL,
    province                                      varchar(2),
    eligibility_indicator                         varchar(1)        NOT NULL,
    eligibility_for_reference_years_indicator     varchar(1)        DEFAULT 'N' NOT NULL,
    yardage_indicator                             varchar(1)        DEFAULT 'N' NOT NULL,
    program_payment_indicator                     varchar(1)        DEFAULT 'N' NOT NULL,
    contract_work_indicator                       varchar(1)        DEFAULT 'N' NOT NULL,
    supply_managed_commodity_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    manual_expense_indicator                      varchar(1)        DEFAULT 'N' NOT NULL,
    exclude_from_revenue_calculation_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    industry_average_expense_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    established_date                              date              NOT NULL,
    expiry_date                                   date              NOT NULL,
    commodity_type_code                           varchar(10),
    fruit_vegetable_type_code                     varchar(10),
    revision_count                                numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                   varchar(30)       NOT NULL,
    create_date                                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                                   varchar(30),
    update_date                                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN line_item.line_item_id IS 'LINE ITEM ID is a surrogate unique identifier for LINE ITEM IDs.'
;
COMMENT ON COLUMN line_item.program_year IS 'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.'
;
COMMENT ON COLUMN line_item.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN line_item.description IS 'DESCRIPTION describes the LINE ITEM.'
;
COMMENT ON COLUMN line_item.province IS 'PROVINCE identifies the province the code is eligible or not eligible in.'
;
COMMENT ON COLUMN line_item.eligibility_indicator IS 'ELIGIBILITY INDICATOR identifies if the LINE ITEM is eligible for the specified province in the program year. For 2012 and past scenarios this affects eligibility for reference years. For 2013 and forward it does not affect reference years.'
;
COMMENT ON COLUMN line_item.eligibility_for_reference_years_indicator IS 'ELIGIBILITY FOR REFERENCE YEARS INDICATOR identifies if the LINE ITEM is eligible for the specified province in the reference year. Used only for 2013 and forward.'
;
COMMENT ON COLUMN line_item.yardage_indicator IS 'YARDAGE INDICATOR identifies if the LINE ITEM is associated with yardage.'
;
COMMENT ON COLUMN line_item.program_payment_indicator IS 'PROGRAM PAYMENT INDICATOR identifies if the LINE ITEM is associated with income or expenses from government programs.'
;
COMMENT ON COLUMN line_item.contract_work_indicator IS 'CONTRACT WORK INDICATOR identifies if the LINE ITEM is associated with contract work.'
;
COMMENT ON COLUMN line_item.supply_managed_commodity_indicator IS 'SUPPLY MANAGED COMMODITY INDICATOR identifies if the LINE ITEM is associated with ''supply managed'' items such as milk or eggs.'
;
COMMENT ON COLUMN line_item.manual_expense_indicator IS 'MANUAL EXPENSE INDICATOR identifies if the LINE ITEM is associated with a manual expense.'
;
COMMENT ON COLUMN line_item.exclude_from_revenue_calculation_indicator IS 'EXCLUDE FROM REVENUE CALCULATION INDICATOR identifies if the LINE ITEM should be excluded when calculating the revenue for an AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN line_item.industry_average_expense_indicator IS 'INDUSTRY AVERAGE EXPENSE INDICATOR identifies if the LINE ITEM is associated with a common expense used to calculate an industry average. This is used by reasonability tests.'
;
COMMENT ON COLUMN line_item.established_date IS 'ESTABLISHED DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN line_item.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN line_item.commodity_type_code IS 'COMMODITY TYPE CODE is a unique code for the object COMMODITY TYPE CODE. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.'
;
COMMENT ON COLUMN line_item.fruit_vegetable_type_code IS 'FRUIT VEGETABLE TYPE CODE is a unique code for the object FRUIT VEGETABLE TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.'
;
COMMENT ON COLUMN line_item.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN line_item.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN line_item.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN line_item.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN line_item.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE line_item IS 'LINE ITEM describes valid incomes and expensed used by Agristability.'
;


CREATE INDEX ix_li_py_efrci ON line_item(program_year, exclude_from_revenue_calculation_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_efryi ON line_item(program_year, eligibility_for_reference_years_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_ei ON line_item(program_year, eligibility_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_ed ON line_item(expiry_date)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_ctc ON line_item(commodity_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_fvtc ON line_item(fruit_vegetable_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_iaei ON line_item(program_year, industry_average_expense_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_cwi ON line_item(program_year, contract_work_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_li ON line_item(program_year, line_item)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_mei ON line_item(program_year, manual_expense_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_ppi ON line_item(program_year, program_payment_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_smci ON line_item(program_year, supply_managed_commodity_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_li_py_yi ON line_item(program_year, yardage_indicator)
 TABLESPACE pg_default
;

ALTER TABLE line_item ADD 
    CONSTRAINT pk_li PRIMARY KEY (line_item_id) USING INDEX TABLESPACE pg_default 
;
