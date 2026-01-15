create or replace procedure farms_bpu_pkg.validate_staging(
   in in_import_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.farm_import_logs
    where import_version_id = in_import_version_id;

    call farms_bpu_pkg.validate_program_year(in_import_version_id);
    call farms_bpu_pkg.validate_municipality(in_import_version_id);
    call farms_bpu_pkg.validate_inventory(in_import_version_id);
    call farms_bpu_pkg.validate_expense(in_import_version_id);
end;
$$;
