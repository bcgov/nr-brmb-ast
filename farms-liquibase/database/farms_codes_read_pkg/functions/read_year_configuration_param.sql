create or replace function farms_codes_read_pkg.read_year_configuration_param(
    in in_parameter_id farms.farm_year_configuration_params.year_configuration_param_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.year_configuration_param_id,
               t.program_year,
               t.parameter_name,
               t.parameter_value,
               t.config_param_type_code,
               c.description,
               t.when_created,
               t.revision_count
        from farms.farm_year_configuration_params t
        inner join farms.farm_config_param_type_codes c on c.config_param_type_code = t.config_param_type_code
        where t.year_configuration_param_id = in_parameter_id;
    return cur;

end;
$$;
