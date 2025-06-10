create or replace function farms_read_pkg.read_sc_logs(
    in sc_id farms.scenario_log.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select sl.log_message,
               sl.create_user,
               sl.create_date
        from farms.scenario_log sl
        where sl.agristability_scenario_id = sc_id
        order by sl.create_date desc;

    return cur;
end;
$$;
