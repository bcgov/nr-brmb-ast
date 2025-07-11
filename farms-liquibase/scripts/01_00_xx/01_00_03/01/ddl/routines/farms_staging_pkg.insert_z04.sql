create or replace procedure farms_staging_pkg.insert_z04(
   in in_income_expense_key farms.z04_income_expenses_detail.income_expense_key%type,
   in in_participant_pin farms.z04_income_expenses_detail.participant_pin%type,
   in in_program_year farms.z04_income_expenses_detail.program_year%type,
   in in_operation_number farms.z04_income_expenses_detail.operation_number%type,
   in in_line_code farms.z04_income_expenses_detail.line_code%type,
   in in_ie_ind farms.z04_income_expenses_detail.ie_indicator%type,
   in in_amount farms.z04_income_expenses_detail.amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z04_income_expenses_detail (
        income_expense_key,
        participant_pin,
        program_year,
        program_year_codified_by,
        operation_number,
        line_code,
        ie_indicator,
        amount,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_income_expense_key,
        in_participant_pin,
        in_program_year,
        in_program_year,
        in_operation_number,
        in_line_code,
        in_ie_ind,
        in_amount,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
