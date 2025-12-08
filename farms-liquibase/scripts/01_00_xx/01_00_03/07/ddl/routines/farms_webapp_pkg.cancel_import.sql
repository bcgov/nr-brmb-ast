create or replace procedure farms_webapp_pkg.cancel_import(
    in in_id farms.farm_import_versions.import_version_id%type
)
language plpgsql
as
$$
begin
    update farms.farm_import_versions
    set import_state_code = 'CAN'
    where import_version_id = in_id;
end;
$$;
