create or replace function farms_webapp_pkg.get_configuration_parameters()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select cp.parameter_name,
               cp.parameter_value
        from farms.farm_configuration_parameters cp
        join farms.farm_config_param_type_codes cptc on cptc.config_param_type_code = cp.config_param_type_code
        order by cp.parameter_name desc;
    return v_cursor;
end;
$$;
