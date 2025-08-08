create or replace procedure farms_version_pkg.perform_import(
   in in_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    r numeric;
begin
    r := farms_import_pkg.run(in_version_id, in_user);
    r := coalesce(r, 0);
    update farms.farm_import_versions
    set import_date = current_date,
        import_state_code = (case when r = 0 then 'IC' else 'IPC' end),
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where import_version_id = in_version_id;
exception
    when others then
        update farms.farm_import_versions
        set import_date = current_date,
            import_state_code = 'IF',
            revision_count = revision_count + 1,
            who_updated = in_user,
            when_updated = current_timestamp
        where import_version_id = in_version_id;
        raise;
end;
$$;
