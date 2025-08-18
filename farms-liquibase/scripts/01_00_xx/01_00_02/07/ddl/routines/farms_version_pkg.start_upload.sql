create or replace procedure farms_version_pkg.start_upload(
    in in_version_id bigint,
    in in_user varchar
)
language plpgsql
as $$
begin

    update farms.farm_import_versions
    set import_state_code = 'SP',
        staging_audit_info = null,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where import_version_id = in_version_id;

end;
$$;
