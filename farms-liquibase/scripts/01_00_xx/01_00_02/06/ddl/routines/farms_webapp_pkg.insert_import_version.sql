create or replace procedure farms_webapp_pkg.insert_import_version(
   out io_import_version_id farms.farm_import_versions.import_version_id%type,
   in in_import_class farms.farm_import_versions.import_class_code%type,
   in in_import_state farms.farm_import_versions.import_state_code%type,
   in in_description farms.farm_import_versions.description%type,
   in in_import_file_name farms.farm_import_versions.import_file_name%type,
   in in_import_file_pwd farms.farm_import_versions.import_file_password%type,
   in in_user farms.farm_import_versions.who_created%type
)
language plpgsql
as $$
begin
    insert into farms.farm_import_versions (
        import_version_id,
        imported_by_user,
        description,
        import_file_name,
        import_file_password,
        import_state_code,
        import_file,
        staging_audit_info,
        import_audit_info,
        import_class_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_iv_seq'),
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
