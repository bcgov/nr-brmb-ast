create or replace function farms_error_pkg.codify_income_expense(
    in msg varchar,
    in line_item farms.farm_line_items.line_item%type,
    in farming_operation_id farms.farm_reported_income_expenses.farming_operation_id%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%fk_rie_fo%' then
        return 'The specified Operation was not found for this Reported Income/Expense ';
    elsif msg like '%fk_rie_as%' then
        return 'The specified Scenario was not found for this Reported Income/Expense item';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
