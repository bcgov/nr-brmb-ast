create or replace function farms_version_pkg.import_complete(
    in in_version_id numeric,
    in in_user varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    update farms.import_version iv
    set iv.import_state_code = 'IC',
        iv.revision_count = iv.revision_count + 1,
        iv.update_user = in_user,
        iv.update_date = current_timestamp,
        import_audit_information = null
    where iv.import_version_id = in_version_id;

    open cur for
        select import_audit_information
        from farms.import_version iv
        where iv.import_version_id = in_version_id;

    return cur;

end;
$$;
