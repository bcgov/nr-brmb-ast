create or replace procedure farms_fmv_pkg.delete_staging_errors(
   in in_import_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.farm_import_logs
    where import_version_id = in_import_version_id;
end;
$$;
