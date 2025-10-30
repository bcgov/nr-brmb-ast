create or replace function farms_webapp_pkg.get_year_configuration_params()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select cp.program_year,
               cp.parameter_name,
               cp.parameter_value
        from farms.farm_year_configuration_params cp
        join farms.farm_config_param_type_codes cptc on cptc.config_param_type_code = cp.config_param_type_code
        order by cp.program_year,
                 cp.parameter_name desc;
    return v_cursor;
end;
$$;
