create or replace procedure farms_codes_write_pkg.update_configuration_parameter(
   in in_config_param_name farms.farm_configuration_parameters.parameter_name%type,
   in in_config_param_value farms.farm_configuration_parameters.parameter_value%type,
   in in_farm_user farms.farm_configuration_parameters.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_configuration_parameters
    set parameter_value = in_config_param_value,
        when_updated = current_timestamp,
        who_updated = in_farm_user,
        revision_count = revision_count + 1
    where parameter_name = in_config_param_name;

end;
$$;
