create or replace function farms_import_pkg.operation(
    in in_program_year_version_id farms.program_year_version.program_year_version_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    op_insert_cursor cursor for
        select z.accounting_code::varchar federal_accounting_code,
               z.business_use_of_home_expenses business_use_home_expense,
               z.crop_disaster_indicator,
               z.crop_share_indicator,
               z.expenses,
               z.feeder_member_indicator,
               to_date(z.fiscal_year_end, 'YYYYMMDD') fiscal_year_end,
               to_date(z.fiscal_year_start, 'YYYYMMDD') fiscal_year_start,
               z.gross_income,
               z.inventory_adjustments,
               z.landlord_indicator,
               z.livestock_disaster_indicator,
               z.net_farm_income,
               z.net_income_after_adjustments,
               z.net_income_before_adjustments,
               z.other_deductions,
               z.partnership_name,
               z.partnership_percent,
               z.partnership_pin,
               z.participant_pin,
               z.operation_number
        from farms.z03_statement_information z
        join farms.agristability_client ac on z.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and z.program_year = py.year
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        where pyv.program_year_version_id = in_program_year_version_id;
    op_insert_val record;

    fo_id numeric;

    ye date;
    ys date;
    ac varchar(10);

    v_operation_number farms.farming_operation.operation_number%type := null;

    error_msg varchar(512) := null;
begin
    -- we always want to make a new operation because this only gets called
    -- when a new program year version is made. We are linking the last version
    -- to determine what the changes are.
    for op_insert_val in op_insert_cursor
    loop
        v_operation_number := op_insert_val.operation_number;

        ye := op_insert_val.fiscal_year_end;
        ys := op_insert_val.fiscal_year_start;
        ac := op_insert_val.federal_accounting_code;

        select nextval('farms.seq_fo')
        into fo_id;

        insert into farms.farming_operation (
            farming_operation_id,
            business_use_home_expense,
            expenses,
            fiscal_year_end,
            fiscal_year_start,
            gross_income,
            inventory_adjustments,
            crop_share_indicator,
            feeder_member_indicator,
            landlord_indicator,
            net_farm_income,
            net_income_after_adjustments,
            net_income_before_adjustments,
            other_deductions,
            partnership_name,
            partnership_percent,
            partnership_pin,
            operation_number,
            crop_disaster_indicator,
            livestock_disaster_indicator,
            locally_updated_indicator,
            federal_accounting_code,
            program_year_version_id,
            alignment_key,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            fo_id,
            op_insert_val.business_use_home_expense,
            op_insert_val.expenses,
            ye,
            ys,
            op_insert_val.gross_income,
            op_insert_val.inventory_adjustments,
            op_insert_val.crop_share_indicator,
            op_insert_val.feeder_member_indicator,
            op_insert_val.landlord_indicator,
            op_insert_val.net_farm_income,
            op_insert_val.net_income_after_adjustments,
            op_insert_val.net_income_before_adjustments,
            op_insert_val.other_deductions,
            op_insert_val.partnership_name,
            op_insert_val.partnership_percent,
            op_insert_val.partnership_pin,
            op_insert_val.operation_number,
            op_insert_val.crop_disaster_indicator,
            op_insert_val.livestock_disaster_indicator,
            'N',
            ac,
            in_program_year_version_id,
            chr(64 + op_insert_val.operation_number::int),
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

        error_msg := farms_import_pkg.operation_partner(fo_id, in_user);
        if error_msg is null then
            error_msg := farms_import_pkg.production_insurance(fo_id, in_user);
        end if;

        if error_msg is not null then
            return error_msg;
        end if;

    end loop;

    return error_msg;
exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_operation(
            sqlerrm,
            v_operation_number,
            ac,
            in_program_year_version_id
        ));
end;
$$;
