create or replace procedure farms_calculator_pkg.update_pi(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_prod_insurance_num varchar[],
    in in_user text
)
language plpgsql
as
$$
declare

    pi_cursor cursor for
        select pi.production_insurance_id,
               pi.production_insurance_number as old_pi_num,
               t.column_value as new_pi_num
        from (
            select unnest(in_prod_insurance_num) as column_value
        ) t
        full outer join (
            select production_insurance_id,
                   production_insurance_number
            from farms.farm_production_insurances
            where farming_operation_id = in_farming_operation_id
        ) pi on t.column_value = pi.production_insurance_number;

    pi_val record;

begin
    for pi_val in pi_cursor
    loop
        if (pi_val.old_pi_num is not null and pi_val.old_pi_num::text <> '') and (pi_val.new_pi_num is not null and pi_val.new_pi_num::text <> '') then
            update farms.farm_production_insurances
            set locally_updated_ind = 'Y',
                revision_count = revision_count + 1,
                when_updated = current_timestamp,
                who_updated = in_user
            where production_insurance_id = pi_val.production_insurance_id;
        else
            if (pi_val.old_pi_num is not null and pi_val.old_pi_num::text <> '') and coalesce(pi_val.new_pi_num::text, '') = '' then
                delete from farms.farm_production_insurances pi
                where pi.production_insurance_id = pi_val.production_insurance_id;
            else
                insert into farms.farm_production_insurances (
                    production_insurance_id,
                    production_insurance_number,
                    locally_updated_ind,
                    farming_operation_id,
                    revision_count,
                    who_created,
                    when_created,
                    who_updated,
                    when_updated
                ) values (
                    nextval('farms.farm_pi_seq'),
                    pi_val.new_pi_num,
                    'Y',
                    in_farming_operation_id,
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );
            end if;
        end if;
    end loop;
end;
$$;
