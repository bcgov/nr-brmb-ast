create or replace procedure farms_codes_write_pkg.update_year_configuration_param(
   in in_parameter_id farms.year_configuration_parameter.year_configuration_parameter_id%type,
   in in_config_param_value farms.year_configuration_parameter.parameter_value%type,
   in in_user farms.year_configuration_parameter.update_user%type
)
language plpgsql
as $$
begin

    update farms.year_configuration_parameter c
    set c.parameter_value = in_config_param_value,
        c.update_date = current_timestamp,
        c.update_user = in_user,
        c.revision_count = c.revision_count + 1
    where c.year_configuration_parameter_id = in_parameter_id;

end;
$$;
