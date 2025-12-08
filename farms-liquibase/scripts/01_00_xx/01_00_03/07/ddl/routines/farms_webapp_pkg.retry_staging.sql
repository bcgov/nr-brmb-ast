create or replace procedure farms_webapp_pkg.retry_staging(
    in in_id farms.farm_import_versions.import_version_id%type
)
language plpgsql
as
$$
begin
    update farms.farm_import_versions
    set import_state_code = 'SS',
        last_status_message  = null,
        last_status_date  = null,
        staging_audit_info = '',
        import_audit_info = ''
    where import_version_id = in_id;
end;
$$;
