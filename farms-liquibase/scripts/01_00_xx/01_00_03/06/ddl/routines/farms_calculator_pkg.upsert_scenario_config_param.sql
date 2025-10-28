create or replace procedure farms_calculator_pkg.upsert_scenario_config_param(
    in in_scenario_id farms.farm_scenario_config_params.agristability_scenario_id%type,
    in in_parameter_name farms.farm_scenario_config_params.parameter_name%type,
    in in_parameter_value farms.farm_scenario_config_params.parameter_value%type,
    in in_user farms.farm_scenario_config_params.who_created%type
)
language plpgsql
as
$$
begin

    merge into farms.farm_scenario_config_params o
    using(
        select in_scenario_id agristability_scenario_id,
        in_parameter_name parameter_name,
        in_parameter_value parameter_value
    ) n
    on o.agristability_scenario_id = n.agristability_scenario_id
    and o.parameter_name = n.parameter_name
    when matched then
        update set
        parameter_value = n.parameter_value,
        when_updated = current_timestamp,
        who_updated = in_user
    when not matched then
        insert (
            scenario_config_param_id,
            parameter_name,
            parameter_value,
            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (nextval('farms.farm_scp_seq'),
            n.parameter_name,
            n.parameter_value,
            in_scenario_id,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

end;
$$;
