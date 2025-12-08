create or replace procedure farms_calculator_pkg.create_ops(
    in new_pyv_id farms.farm_farming_operations.program_year_version_id%type,
    in num_operations bigint,
    in cur_user farms.farm_farming_operations.who_created%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_farming_operations (
        farming_operation_id,
        crop_share_ind,
        feeder_member_ind,
        landlord_ind,
        partnership_percent,
        partnership_pin,
        operation_number,
        alignment_key,
        locally_updated_ind,
        locally_generated_ind,
        program_year_version_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select nextval('farms.farm_fo_seq') farming_operation_id,
           'N' crop_share_ind,
           'N' feeder_member_ind,
           'N' landlord_ind,
           1 partnership_percent,
           0 partnership_pin,
           operation_number,
           chr(64 + operation_number) alignment_key,
           'Y' locally_updated_ind,
           'Y' locally_generated_ind,
           new_pyv_id program_year_version_id,
           1 revision_count,
           cur_user who_created,
           current_timestamp when_created,
           cur_user who_updated,
           current_timestamp when_updated
    from (select generate_series(1, coalesce(num_operations, 0)) as operation_number) t;

end;
$$;
