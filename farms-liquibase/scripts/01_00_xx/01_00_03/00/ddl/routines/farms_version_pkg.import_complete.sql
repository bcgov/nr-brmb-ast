drop function if exists farms_version_pkg.import_complete(
    numeric,
    varchar
);

create or replace procedure farms_version_pkg.import_complete(
    in in_version_id numeric,
    in in_message varchar,
    in in_user varchar
)
language plpgsql
as $$
begin

    update farms.farm_import_versions iv
    set iv.import_state_code = 'IC',
        iv.revision_count = iv.revision_count + 1,
        iv.who_updated = in_user,
        iv.when_updated = current_timestamp,
        import_audit_info = in_message
    where iv.import_version_id = in_version_id;

end;
$$;
