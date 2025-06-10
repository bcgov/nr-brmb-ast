create or replace function farms_version_pkg.create_version(
   in in_description farms.import_version.description%type,
   in in_import_file_name farms.import_version.import_file_name%type,
   in in_user varchar
)
returns integer
language plpgsql
as $$
declare
    r integer;
begin
    select nextval('farms.seq_iv')
    into r;

    insert into farms.import_version (
        import_version_id,
        imported_by_user,
        description,
        import_file_name,
        import_state_code,
        import_class_code,
        staging_audit_information,
        import_audit_information,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        r,
        in_user,
        in_description,
        in_import_file_name,
        'SS',
        'CRA',
        null,
        null,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    update farms.import_version
    set import_state_code = 'CAN'
    where import_state_code = 'SC';

    return r;
end;
$$;
