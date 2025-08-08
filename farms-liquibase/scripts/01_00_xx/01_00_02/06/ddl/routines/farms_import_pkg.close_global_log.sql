create or replace procedure farms_import_pkg.close_global_log(
   in in_version_id numeric
)
language plpgsql
as $$
declare
    g1_cursor cursor for
        select t.log_message msg
        from farms.farm_import_logs t
        where t.import_version_id = in_version_id
        order by t.import_log_id asc;
    g1_val record;
begin
    for g1_val in g1_cursor
    loop
        update farms.farm_import_versions
        set import_audit_info = import_audit_info || g1_val.msg
        where import_version_id = in_version_id;
    end loop;

    delete from farms.farm_import_logs
    where import_version_id = in_version_id;
end;
$$;
