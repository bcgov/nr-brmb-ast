create or replace procedure farms_version_pkg.import_complete(
    in in_version_id bigint,
    in in_message varchar,
    in in_user varchar
)
language plpgsql
as $$
begin

    update farms.farm_import_versions
    set import_state_code = 'IC',
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp,
        import_audit_info = in_message
    where import_version_id = in_version_id;

end;
$$;
