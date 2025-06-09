create or replace function farms_codes_read_pkg.read_year_configuration_params(
    in in_program_year farms.year_configuration_parameter.program_year%type
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
               t.create_user,
               t.create_date,
               t.update_user,
               t.update_date,
               t.revision_count,
               c.description
        from farms.year_configuration_parameter t
        inner join farms.configuration_parameter_type_code c on c.configuration_parameter_type_code = t.configuration_parameter_type_code
        where t.program_year = in_program_year
        order by lower(t.parameter_name);
    return cur;

end;
$$;
