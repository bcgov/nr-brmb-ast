create or replace function farms_read_pkg.read_sc_logs(
    in sc_id farms.farm_scenario_logs.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select sl.log_message,
               sl.who_created,
               sl.when_created
        from farms.farm_scenario_logs sl
        where sl.agristability_scenario_id = sc_id
        order by sl.when_created desc;

    return cur;
end;
$$;
