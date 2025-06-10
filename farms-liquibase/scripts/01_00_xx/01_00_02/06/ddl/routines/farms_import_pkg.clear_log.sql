create or replace procedure farms_import_pkg.clear_log(
   in in_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.import_log
    where import_version_id = in_version_id;

    -- audit stuff will be taken care of in the main body
    update farms.import_version
    set import_audit_information = null
    where import_version_id = in_version_id;

    commit;
end;
$$;
