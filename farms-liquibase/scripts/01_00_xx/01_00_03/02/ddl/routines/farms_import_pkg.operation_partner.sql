create or replace function farms_import_pkg.operation_partner(
    in in_farming_operation_id farms.farming_operation.farming_operation_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    ptr_insert_cursor cursor for
        select z.partner_sin_ctn_business_number partner_sin,
               z.partner_first_name first_name,
               z.partner_last_name last_name,
               z.partner_corp_name corp_name,
               z.partner_percent,
               z.partner_pin
        from farms.z05_partner_information z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
                                        and op.operation_number = z.operation_number
        where op.farming_operation_id = in_farming_operation_id;
    ptr_insert_val record;

    part_id numeric;
begin
    for ptr_insert_val in ptr_insert_cursor
    loop
        -- always insert, history is just for reporting
        select nextval('farms.seq_fop')
        into part_id;

        insert into farms.farming_operatin_partner (
            farming_operation_partner_id,
            partner_percent,
            partnership_pin,
            partner_sin,
            first_name,
            last_name,
            corp_name,
            farming_operation_id,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            part_id,
            ptr_insert_val.partner_percent,
            ptr_insert_val.partner_pin,
            ptr_insert_val.partner_sin,
            ptr_insert_val.first_name,
            ptr_insert_val.last_name,
            ptr_insert_val.corp_name,
            in_farming_operation_id,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );
    end loop;

    return null;
exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_operational_partner(
            sqlerrm,
            in_farming_operation_id
        ));
end;
$$;
