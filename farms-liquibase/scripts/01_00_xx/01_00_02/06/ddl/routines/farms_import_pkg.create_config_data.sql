create or replace procedure farms_import_pkg.create_config_data(
   in in_user farms.farm_program_years.who_created%type
)
language plpgsql
as $$
begin

    call farms_import_pkg.inventory_details(in_user);
    call farms_import_pkg.year_configuration_params(in_user);
    call farms_import_pkg.line_items(in_user);

end;
$$;
