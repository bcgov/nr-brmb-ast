create or replace procedure farms_codes_write_pkg.update_configuration_parameter(
   in in_config_param_name farms.configuration_parameter.parameter_name%type,
   in in_config_param_value farms.configuration_parameter.parameter_value%type,
   in in_farm_user farms.configuration_parameter.update_user%type
)
language plpgsql
as $$
begin

    update farms.configuration_parameter c
    set c.parameter_value = in_config_param_value,
        c.update_date = current_timestamp,
        c.update_user = in_farm_user,
        c.revision_count = c.revision_count + 1
    where c.parameter_name = in_config_param_name;

end;
$$;
