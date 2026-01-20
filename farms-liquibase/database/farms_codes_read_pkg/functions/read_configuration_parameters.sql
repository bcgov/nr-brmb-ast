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
               t.sensitive_data_ind,
               t.config_param_type_code,
               t.who_created,
               t.when_created,
               t.who_updated,
               t.when_updated,
               t.revision_count,
               c.description
        from farms.farm_configuration_parameters t
        inner join farms.farm_config_param_type_codes c on c.config_param_type_code = t.config_param_type_code
        order by lower(t.parameter_name);
    return cur;

end;
$$;
