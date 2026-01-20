create or replace function farms_webapp_pkg.get_sc_rev_count(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns refcursor
language plpgsql
as
$$
declare

    return_cur refcursor;

begin
    open return_cur for
        select s.revision_count
        from farms.farm_agristability_scenarios s
        where s.agristability_scenario_id = in_scenario_id;

    return return_cur;
end;
$$;
