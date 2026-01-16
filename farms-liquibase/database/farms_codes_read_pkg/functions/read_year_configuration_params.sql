create or replace function farms_codes_read_pkg.read_year_configuration_params(
    in in_program_year farms.farm_year_configuration_params.program_year%type
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
               t.who_created,
               t.when_created,
               t.who_updated,
               t.when_updated,
               t.revision_count,
               c.description
        from farms.farm_year_configuration_params t
        inner join farms.farm_config_param_type_codes c on c.config_param_type_code = t.config_param_type_code
        where t.program_year = in_program_year
        order by lower(t.parameter_name);
    return cur;

end;
$$;
