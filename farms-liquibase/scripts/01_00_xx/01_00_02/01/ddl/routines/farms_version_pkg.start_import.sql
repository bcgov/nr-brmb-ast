create or replace procedure farms_version_pkg.start_import(
   in in_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
begin

    update farms.import_version
    set import_state_code = 'IP',
        import_audit_information = null,
        revision_count = revision_count + 1,
        update_user = in_user,
        update_date = current_timestamp
    where import_version_id = in_version_id;
end;
$$;
