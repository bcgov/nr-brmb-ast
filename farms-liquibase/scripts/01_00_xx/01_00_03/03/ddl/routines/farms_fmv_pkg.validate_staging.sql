create or replace procedure farms_fmv_pkg.validate_staging(
   in in_import_version_id numeric
)
language plpgsql
as $$
begin
    delete from farms.farm_import_logs
    where import_version_id = in_import_version_id;

    call farms_fmv_pkg.validate_program_year(in_import_version_id);
    call farms_fmv_pkg.validate_period(in_import_version_id);
    call farms_fmv_pkg.validate_variance(in_import_version_id);
    call farms_fmv_pkg.validate_municipality(in_import_version_id);
    call farms_fmv_pkg.validate_unit(in_import_version_id);
    call farms_fmv_pkg.validate_inventory(in_import_version_id);
    call farms_fmv_pkg.validate_xref_exists(in_import_version_id);
    call farms_fmv_pkg.validate_uniqueness(in_import_version_id);
    call farms_fmv_pkg.validate_one_unit_per_inventry(in_import_version_id);
    call farms_fmv_pkg.validate_defaults(in_import_version_id);
end;
$$;
