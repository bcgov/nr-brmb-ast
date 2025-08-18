create or replace function farms_version_pkg.import_failure(
    in in_version_id bigint,
    in in_user varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    update farms.farm_import_versions
    set import_state_code = 'IF',
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp,
        import_audit_info = null
    where import_version_id = in_version_id;

    open cur for
        select import_audit_info
        from farms.farm_import_versions
        where import_version_id = in_version_id;

    return cur;

end;
$$;
