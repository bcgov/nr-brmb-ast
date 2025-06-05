create or replace function farms_codes_read_pkg.read_year_configuration_param(
    in in_parameter_id farms.year_configuration_parameter.year_configuration_parameter_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.year_configuration_parameter_id,
               t.program_year,
               t.parameter_name,
               t.parameter_value,
               t.configuration_parameter_type_code,
               c.description,
               t.create_date,
               t.revision_count
        from farms.year_configuration_parameter t
        inner join farms.configuration_parameter_type_code c on c.configuration_parameter_type_code = t.configuration_parameter_type_code
        where t.year_configuration_parameter_id = in_parameter_id;
    return cur;

end;
$$;
