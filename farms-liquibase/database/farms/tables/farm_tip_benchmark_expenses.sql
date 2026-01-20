CREATE TABLE farms.farm_tip_benchmark_expenses (
	tip_benchmark_expense_id bigint NOT NULL,
	line_item smallint NOT NULL,
	description varchar(256) NOT NULL,
	eligibility_ind varchar(1) NOT NULL DEFAULT 'N',
	amount decimal(16,2) NOT NULL,
	amount_per_100 decimal(16,2) NOT NULL,
	amount_per_100_high_25_pct decimal(16,2) NOT NULL,
	amount_per_100_5_year_average decimal(16,2),
	tip_benchmark_year_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_benchmark_expenses IS E'TIP BENCHMARK EXPENSE contains the benchmark values for a group of farming operations within a farm type and income range, for a specific expense.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount IS E'AMOUNT is the total amount of expense for this LINE ITEM.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100 IS E'AMOUNT PER 100 is the benchmark amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI) for this grouping of farming operations.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100_5_year_average IS E'AMOUNT PER 100 5 YEAR AVERAGE is the 5-year average of the benchmark amount of expense for this LINE ITEM per $100 of Gross Farming Income (GFI) for this grouping of farming operations.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.amount_per_100_high_25_pct IS E'AMOUNT PER 100 HIGH 25 PCT is the higher 25th percentile for this benchmark group.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.description IS E'DESCRIPTION describes the LINE ITEM.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.eligibility_ind IS E'ELIGIBILITY IND identifies if the LINE ITEM is eligible for the specified province in the program year. For 2012 and past scenarios this affects eligibility for reference years. For 2013 and forward it does not affect reference years.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.line_item IS E'LINE ITEM is income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.tip_benchmark_expense_id IS E'TIP BENCHMARK EXPENSE ID is a surrogate unique identifier for TIP BENCHMARK EXPENSE.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.tip_benchmark_year_id IS E'TIP BENCHMARK YEAR ID is a surrogate unique identifier for TIP BENCHMARK YEAR.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_benchmark_expenses.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_tip_benchmark_expenses ADD CONSTRAINT farm_tbe_pk PRIMARY KEY (tip_benchmark_expense_id);
ALTER TABLE farms.farm_tip_benchmark_expenses ADD CONSTRAINT farm_tbe_uk UNIQUE (tip_benchmark_year_id,line_item);
ALTER TABLE farms.farm_tip_benchmark_expenses ADD CONSTRAINT farm_tbe_eligibility_chk CHECK (eligibility_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN tip_benchmark_expense_id SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN description SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN eligibility_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN amount SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN amount_per_100 SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN amount_per_100_high_25_pct SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN tip_benchmark_year_id SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_benchmark_expenses ALTER COLUMN when_created SET NOT NULL;
