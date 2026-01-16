create or replace procedure farms_calculator_pkg.update_op_schedule(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_alignment_key farms.farm_farming_operations.alignment_key%type,
    in in_revision_count farms.farm_farming_operations.revision_count%type,
    in in_user text
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin

    update farms.farm_farming_operations
    set alignment_key = in_alignment_key,
        who_updated = in_user,
        when_updated = current_timestamp,
        revision_count = revision_count + 1
    where farming_operation_id = in_farming_operation_id
    and revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;

end;
$$;
