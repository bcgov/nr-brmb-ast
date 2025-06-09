create or replace function farms_codes_read_pkg.read_configuration_parameters()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.configuration_parameter_id,
               t.parameter_name,
               t.parameter_value,
               t.sensitive_data_indicator,
               t.configuration_parameter_type_code,
               t.create_user,
               t.create_date,
               t.update_user,
               t.update_date,
               t.revision_count,
               c.description
        from farms.configuration_parameter t
        inner join farms.configuration_parameter_type_code c on c.configuration_parameter_type_code = t.configuration_parameter_type_code
        order by lower(t.parameter_name);
    return cur;

end;
$$;
