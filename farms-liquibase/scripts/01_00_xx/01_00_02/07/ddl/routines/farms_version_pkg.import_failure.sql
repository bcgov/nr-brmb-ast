create or replace function farms_version_pkg.import_failure(
    in in_version_id numeric,
    in in_user varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    update farms.farm_import_versions iv
    set iv.import_state_code = 'IF',
        iv.revision_count = iv.revision_count + 1,
        iv.who_updated = in_user,
        iv.when_updated = current_timestamp,
        import_audit_info = null
    where iv.import_version_id = in_version_id;

    open cur for
        select import_audit_info
        from farms.farm_import_versions iv
        where iv.import_version_id = in_version_id;

    return cur;

end;
$$;
