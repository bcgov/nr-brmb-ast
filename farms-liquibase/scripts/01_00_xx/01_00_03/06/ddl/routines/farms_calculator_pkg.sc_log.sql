create or replace procedure farms_calculator_pkg.sc_log(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_log_message farms.farm_scenario_logs.log_message%type,
    in in_user farms.farm_agristability_scenarios.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_scenario_logs (
        scenario_log_id,
        log_message,
        agristability_scenario_id,
        who_created,
        who_updated
    ) values (
        nextval('farms.farm_sl_seq'),
        in_log_message,
        in_scenario_id,
        in_user,
        in_user
    );

end;
$$;
