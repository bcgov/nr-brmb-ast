create or replace function farms_version_pkg.create_version(
   in in_description farms.farm_import_versions.description%type,
   in in_import_file_name farms.farm_import_versions.import_file_name%type,
   in in_user varchar
)
returns integer
language plpgsql
as $$
declare
    r integer;
begin
    select nextval('farms.farm_iv_seq')
    into r;

    insert into farms.farm_import_versions (
        import_version_id,
        imported_by_user,
        description,
        import_file_name,
        import_state_code,
        import_class_code,
        staging_audit_info,
        import_audit_info,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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

    update farms.farm_import_versions
    set import_state_code = 'CAN'
    where import_state_code = 'SC';

    return r;
end;
$$;
