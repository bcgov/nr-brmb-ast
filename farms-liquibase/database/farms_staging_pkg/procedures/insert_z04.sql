create or replace procedure farms_staging_pkg.insert_z04(
   in in_income_expense_key farms.farm_z04_income_exps_dtls.income_expense_key%type,
   in in_participant_pin farms.farm_z04_income_exps_dtls.participant_pin%type,
   in in_program_year farms.farm_z04_income_exps_dtls.program_year%type,
   in in_operation_number farms.farm_z04_income_exps_dtls.operation_number%type,
   in in_line_code farms.farm_z04_income_exps_dtls.line_code%type,
   in in_ie_ind farms.farm_z04_income_exps_dtls.ie_ind%type,
   in in_amount farms.farm_z04_income_exps_dtls.amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z04_income_exps_dtls (
        income_expense_key,
        participant_pin,
        program_year,
        program_year_codified_by,
        operation_number,
        line_code,
        ie_ind,
        amount,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
