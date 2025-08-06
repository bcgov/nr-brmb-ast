create or replace procedure farms_import_pkg.clear_log(
   in in_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.farm_import_logs
    where import_version_id = in_version_id;

    -- audit stuff will be taken care of in the main body
    update farms.farm_import_versions
    set import_audit_info = null
    where import_version_id = in_version_id;

    commit;
end;
$$;
