drop function if exists farms_version_pkg.import_failure(
    numeric,
    varchar
);

create or replace procedure farms_version_pkg.import_failure(
    in in_version_id numeric,
    in in_message varchar,
    in in_user varchar
)
language plpgsql
as $$
begin

    update farms.import_version iv
    set iv.import_state_code = 'IF',
        iv.revision_count = iv.revision_count + 1,
        iv.update_user = in_user,
        iv.update_date = current_timestamp,
        import_audit_information = in_message
    where iv.import_version_id = in_version_id;

end;
$$;
