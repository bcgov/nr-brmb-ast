create or replace procedure farms_bpu_pkg.delete_staging_errors(
   in in_import_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.import_log
    where import_version_id = in_import_version_id;
end;
$$;
