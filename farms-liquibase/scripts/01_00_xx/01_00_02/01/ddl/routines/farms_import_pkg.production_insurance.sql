create or replace function farms_import_pkg.production_insurance(
    in in_farming_operation_id farms.farming_operation.farming_operation_id%type,
    in in_user varchar
)
returns numeric
language plpgsql
as $$
declare
    pi_insert_cursor cursor for
        select z.operation_number,
               z.production_insurance_number
        from farms.z22_production_insurance z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and z.program_year = py.year
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
                                        and z.operation_number = op.operation_number
        where op.farming_operation_id = in_farming_operation_id;
    pi_insert_val record;

    p_id numeric;
begin
    for pi_insert_val in pi_insert_cursor
    loop
        select nextval('farms.seq_pi')
        into p_id;

        insert into farms.production_insurance (
            production_insurance_id,
            production_insurance_number,
            locally_updated_indicator,
            farming_operation_id,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            p_id,
            pi_insert_val.production_insurance_number,
            'N',
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
        return farms_import_pkg.scrub(farms.farms_error_pkg.codify_production_insurance(
            sqlerrm,
            in_farming_operation_id
        ));
end;
$$;
