CREATE TABLE farms.farm_tip_benchmark_expenses(
    tip_benchmark_expense_id             numeric(10, 0)    NOT NULL,
    line_item                            numeric(4, 0)     NOT NULL,
    description                          varchar(256)      NOT NULL,
    eligibility_ind                varchar(1)        DEFAULT 'N' NOT NULL,
    amount                               numeric(16, 2)    NOT NULL,
    amount_per_100                       numeric(16, 2)    NOT NULL,
    amount_per_100_high_25_pct    numeric(16, 2)    NOT NULL,
    amount_per_100_5_year_average        numeric(16, 2),
    tip_benchmark_year_id                numeric(10, 0)    NOT NULL,
    revision_count                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                          varchar(30)       NOT NULL,
    when_created                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                          varchar(30),
    when_updated                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.tip_benchmark_expense_id IS 'TIP BENCHMARK EXPENSE ID is a surrogate unique identifier for TIP BENCHMARK EXPENSE.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.description IS 'DESCRIPTION describes the LINE ITEM.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.eligibility_ind IS 'ELIGIBILITY INDICATOR identifies if the LINE ITEM is eligible for the specified province in the program year. For 2012 and past scenarios this affects eligibility for reference years. For 2013 and forward it does not affect reference years.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount IS 'AMOUNT is the total amount of expense for this LINE ITEM.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100 IS 'AMOUNT PER 100 is the benchmark amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI) for this grouping of farming operations.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100_high_25_pct IS 'AMOUNT PER 100 HIGH 25 PCT is the higher 25th percentile for this benchmark group.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100_5_year_average IS 'AMOUNT PER 100 5 YEAR AVERAGE is the 5-year average of the benchmark amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI) for this grouping of farming operations.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.tip_benchmark_year_id IS 'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_tip_benchmark_expenses IS 'TIP BENCHMARK EXPENSE contains the benchmark values for a group of farming operations within a farm type and income range, for a specific expense.'
;


CREATE UNIQUE INDEX uk_tbe_tbyi_li ON farms.farm_tip_benchmark_expenses(tip_benchmark_year_id, line_item)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_tip_benchmark_expenses ADD 
    CONSTRAINT pk_tbe PRIMARY KEY (tip_benchmark_expense_id) USING INDEX TABLESPACE pg_default 
;
