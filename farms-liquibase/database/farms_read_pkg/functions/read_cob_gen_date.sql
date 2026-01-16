create or replace function farms_read_pkg.read_cob_gen_date(
    in scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select docs.generation_date
        from farms.farm_benefit_calc_documents docs
        where docs.agristability_scenario_id = scenario_id;
    return cur;
end;
$$;
