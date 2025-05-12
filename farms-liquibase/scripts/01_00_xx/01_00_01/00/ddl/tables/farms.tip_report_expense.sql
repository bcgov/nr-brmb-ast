CREATE TABLE farms.tip_report_expense(
    tip_report_expense_id                numeric(10, 0)    NOT NULL,
    line_item                            numeric(4, 0)     NOT NULL,
    description                          varchar(256)      NOT NULL,
    eligibility_indicator                varchar(1)        NOT NULL,
    amount                               numeric(16, 2)    NOT NULL,
    amount_per_100                       numeric(16, 2)    NOT NULL,
    amount_per_100_5_year                numeric(16, 2),
    amount_per_100_benchmark             numeric(16, 2),
    amount_per_100_high_25_percentile    numeric(16, 2),
    tip_rating_code                      varchar(10),
    tip_report_result_id                 numeric(10, 0)    NOT NULL,
    revision_count                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                          varchar(30)       NOT NULL,
    create_date                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                          varchar(30),
    update_date                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.tip_report_expense.tip_report_expense_id IS 'TIP REPORT EXPENSE ID is a surrogate unique identifier for TIP REPORT EXPENSE.'
;
COMMENT ON COLUMN farms.tip_report_expense.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN farms.tip_report_expense.description IS 'DESCRIPTION describes the LINE ITEM.'
;
COMMENT ON COLUMN farms.tip_report_expense.eligibility_indicator IS 'ELIGIBILITY INDICATOR identifies if the LINE ITEM is eligible for the specified province in the program year. For 2012 and past scenarios this affects eligibility for reference years. For 2013 and forward it does not affect reference years.'
;
COMMENT ON COLUMN farms.tip_report_expense.amount IS 'AMOUNT is the total amount of expense for this LINE ITEM.'
;
COMMENT ON COLUMN farms.tip_report_expense.amount_per_100 IS 'AMOUNT PER 100 is the amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI).'
;
COMMENT ON COLUMN farms.tip_report_expense.amount_per_100_5_year IS 'AMOUNT PER 100 5 YEAR is the 5-year average of the amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI) for this operation.'
;
COMMENT ON COLUMN farms.tip_report_expense.amount_per_100_benchmark IS 'AMOUNT PER 100 BENCHMARK is the industry benchmark amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI). The benchmark is an average of farming operations with similar revenues and commodities sold.'
;
COMMENT ON COLUMN farms.tip_report_expense.amount_per_100_high_25_percentile IS 'AMOUNT PER 100 HIGH 25 PERCENTILE is the higher 25th percentile of AMOUNT PER 100 for this benchmark group.'
;
COMMENT ON COLUMN farms.tip_report_expense.tip_rating_code IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.tip_report_expense.tip_report_result_id IS 'TIP REPORT RESULT ID is a surrogate unique identifier for TIP REPORT RESULT.'
;
COMMENT ON COLUMN farms.tip_report_expense.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.tip_report_expense.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.tip_report_expense.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.tip_report_expense.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.tip_report_expense.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.tip_report_expense IS 'TIP REPORT EXPENSE contains the results of a TIP Report run against a FARMING OPERATION, for a specific expense.'
;


CREATE INDEX ix_tre_trc ON farms.tip_report_expense(tip_rating_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tre_trri_li ON farms.tip_report_expense(tip_report_result_id, line_item)
 TABLESPACE pg_default
;

ALTER TABLE farms.tip_report_expense ADD 
    CONSTRAINT pk_tre PRIMARY KEY (tip_report_expense_id) USING INDEX TABLESPACE pg_default 
;
