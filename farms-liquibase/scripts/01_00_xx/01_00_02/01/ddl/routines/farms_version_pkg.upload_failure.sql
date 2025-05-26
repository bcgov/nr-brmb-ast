create or replace procedure farms_version_pkg.upload_failure(
   in in_version_id numeric,
   in in_message varchar,
   in in_user varchar
)
language plpgsql
as $$
begin

    update farms.import_version
    set import_state_code = 'SF',
        revision_count = revision_count + 1,
        update_user = in_user,
        update_date = current_timestamp,
        staging_audit_information = in_message
    where import_version_id = in_version_id;
end;
$$;
