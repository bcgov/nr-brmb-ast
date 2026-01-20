CREATE TABLE farms.farm_reported_income_expenses (
	reported_income_expense_id bigint NOT NULL,
	amount decimal(13,2) NOT NULL,
	expense_ind char(1) NOT NULL,
	line_item smallint NOT NULL,
	import_comment varchar(128),
	farming_operation_id bigint NOT NULL,
	agristability_scenario_id bigint,
	cra_reported_income_expense_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_reported_income_expenses IS E'REPORTED INCOME EXPENSE data refers to data submitted on a client''s tax return (statement). REPORTED INCOME EXPENSE  data will originate from the federal imports. There may be many REPORTED INCOME EXPENSE  records for a given FARMING OPERATION- i.e. multiple income and expense line items on a tax return. REPORTED INCOME EXPENSE  items may have many instances of an INCOME EXPENSE ADJUSTMENT.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.amount IS E'AMOUNT is the Income/Expense Amount (not adjusted for prshp pct).';
COMMENT ON COLUMN farms.farm_reported_income_expenses.cra_reported_income_expense_id IS E'REPORTED INCOME EXPENSE ID is a surrogate unique identifier for REPORTED INCOME EXPENSEs.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.expense_ind IS E'EXPENSE IND is the indator for the Income or Expense (''N'' or ''Y'').';
COMMENT ON COLUMN farms.farm_reported_income_expenses.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.import_comment IS E'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.line_item IS E'LINE ITEM is income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.reported_income_expense_id IS E'REPORTED INCOME EXPENSE ID is a surrogate unique identifier for REPORTED INCOME EXPENSEs.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_reported_income_expenses.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rie_farm_as_fk_i ON farms.farm_reported_income_expenses (agristability_scenario_id);
CREATE INDEX farm_rie_farm_fo_fk_i ON farms.farm_reported_income_expenses (farming_operation_id);
CREATE INDEX farm_rie_farm_rie_fk_i ON farms.farm_reported_income_expenses (cra_reported_income_expense_id);
CREATE INDEX farm_rie_li_i ON farms.farm_reported_income_expenses (line_item);
ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT farm_rie_pk PRIMARY KEY (reported_income_expense_id);
ALTER TABLE farms.farm_reported_income_expenses ADD CONSTRAINT avcon_1299283225_expen_000 CHECK (expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN reported_income_expense_id SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN amount SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN expense_ind SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_reported_income_expenses ALTER COLUMN when_created SET NOT NULL;
