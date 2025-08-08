create or replace procedure farms_codes_write_pkg.update_year_configuration_param(
   in in_parameter_id farms.farm_year_configuration_params.year_configuration_param_id%type,
   in in_config_param_value farms.farm_year_configuration_params.parameter_value%type,
   in in_user farms.farm_year_configuration_params.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_year_configuration_params
    set parameter_value = in_config_param_value,
        when_updated = current_timestamp,
        who_updated = in_user,
        revision_count = revision_count + 1
    where year_configuration_param_id = in_parameter_id;

end;
$$;
