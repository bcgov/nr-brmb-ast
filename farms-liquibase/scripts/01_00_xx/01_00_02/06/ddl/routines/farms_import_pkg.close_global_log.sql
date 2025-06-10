create or replace procedure farms_import_pkg.close_global_log(
   in in_version_id numeric
)
language plpgsql
as $$
declare
    g1_cursor cursor for
        select t.log_message msg
        from farms.import_log t
        where t.import_version_id = in_version_id
        order by t.import_log_id asc;
    g1_val record;
begin
    for g1_val in g1_cursor
    loop
        update farms.import_version
        set import_audit_information = import_audit_information || g1_val.msg
        where import_version_id = in_version_id;
    end loop;

    delete from farms.import_log
    where import_version_id = in_version_id;
end;
$$;
