CREATE TABLE reported_income_expenses(
    reported_income_expense_id        numeric(10, 0)    NOT NULL,
    amount                            numeric(13, 2)    NOT NULL,
    expense_indicator                 character(1)      NOT NULL,
    line_item                         numeric(4, 0)     NOT NULL,
    import_comment                    varchar(128),
    farming_operation_id              numeric(10, 0)    NOT NULL,
    agristability_scenario_id         numeric(10, 0),
    cra_reported_income_expense_id    numeric(10, 0),
    revision_count                    numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                       varchar(30)       NOT NULL,
    create_date                       timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                       varchar(30),
    update_date                       timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reported_income_expenses.reported_income_expense_id IS 'REPORTED INCOME EXPENSE ID is a surrogate unique identifier for REPORTED INCOME EXPENSEs.'
;
COMMENT ON COLUMN reported_income_expenses.amount IS 'AMOUNT is the Income/Expense Amount (not adjusted for prshp pct).'
;
COMMENT ON COLUMN reported_income_expenses.expense_indicator IS 'EXPENSE INDICATOR is the indicator for the Income or Expense (''N'' or ''Y'').'
;
COMMENT ON COLUMN reported_income_expenses.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN reported_income_expenses.import_comment IS 'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.'
;
COMMENT ON COLUMN reported_income_expenses.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN reported_income_expenses.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN reported_income_expenses.cra_reported_income_expense_id IS 'REPORTED INCOME EXPENSE ID is a surrogate unique identifier for REPORTED INCOME EXPENSEs.'
;
COMMENT ON COLUMN reported_income_expenses.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reported_income_expenses.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reported_income_expenses.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reported_income_expenses.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reported_income_expenses.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reported_income_expenses IS 'REPORTED INCOME EXPENSE data refers to data submitted on a client''s tax return (statement). REPORTED INCOME EXPENSE  data will originate from the federal imports. There may be many REPORTED INCOME EXPENSE  records for a given FARMING OPERATION- i.e. multiple income and expense line items on a tax return. REPORTED INCOME EXPENSE  items may have many instances of an INCOME EXPENSE ADJUSTMENT.'
;


CREATE INDEX ix_rie_asi ON reported_income_expenses(agristability_scenario_id)
;
CREATE INDEX ix_rie_foi ON reported_income_expenses(farming_operation_id)
;
CREATE INDEX ix_rie_criei ON reported_income_expenses(cra_reported_income_expense_id)
;
CREATE INDEX ix_rie_li ON reported_income_expenses(line_item)
;
