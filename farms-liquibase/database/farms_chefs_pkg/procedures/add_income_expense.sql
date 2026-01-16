create or replace procedure farms_chefs_pkg.add_income_expense(
    in in_reported_amount farms.farm_reported_income_expenses.amount%type,
    in in_expense_ind farms.farm_reported_income_expenses.expense_ind%type,
    in in_farming_operation_id farms.farm_reported_income_expenses.farming_operation_id%type,
    in in_line_item farms.farm_reported_income_expenses.line_item%type,
    in in_user farms.farm_reported_income_expenses.who_updated%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_reported_income_expenses (
        reported_income_expense_id,
        amount,
        expense_ind,
        farming_operation_id,
        line_item,
        who_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rie_seq'),
        in_reported_amount,
        in_expense_ind,
        in_farming_operation_id,
        in_line_item,
        in_user,
        in_user,
        current_timestamp
    );

end;
$$;
