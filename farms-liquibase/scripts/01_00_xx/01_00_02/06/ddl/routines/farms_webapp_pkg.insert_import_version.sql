create or replace procedure farms_webapp_pkg.insert_import_version(
   out io_import_version_id farms.import_version.import_version_id%type,
   in in_import_class farms.import_version.import_class_code%type,
   in in_import_state farms.import_version.import_state_code%type,
   in in_description farms.import_version.description%type,
   in in_import_file_name farms.import_version.import_file_name%type,
   in in_import_file_pwd farms.import_version.import_file_password%type,
   in in_user farms.import_version.create_user%type
)
language plpgsql
as $$
begin
    insert into farms.import_version (
        import_version_id,
        imported_by_user,
        description,
        import_file_name,
        import_file_password,
        import_state_code,
        import_file,
        staging_audit_information,
        import_audit_information,
        import_class_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        nextval('farms.seq_iv'),
        in_user,
        in_description,
        in_import_file_name,
        null,
        in_import_state,
        null,
        null,
        null,
        in_import_class,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    ) returning import_version_id into io_import_version_id;
end;
$$;
