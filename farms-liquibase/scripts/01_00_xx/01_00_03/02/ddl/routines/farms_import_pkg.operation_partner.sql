create or replace function farms_import_pkg.operation_partner(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    ptr_insert_cursor cursor for
        select z.partner_sin_ctn_bn partner_sin,
               z.partner_first_name first_name,
               z.partner_last_name last_name,
               z.partner_corp_name corp_name,
               z.partner_percent,
               z.partner_pin
        from farms.farm_z05_partner_infos z
        join farms.farm_agristability_clients ac on ac.participant_pin = z.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_farming_operations op on op.program_year_version_id = pyv.program_year_version_id
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

        insert into farms.farm_farming_operatin_prtnrs (
            farming_operatin_prtnr_id,
            partner_percent,
            partnership_pin,
            partner_sin,
            first_name,
            last_name,
            corp_name,
            farming_operation_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
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
