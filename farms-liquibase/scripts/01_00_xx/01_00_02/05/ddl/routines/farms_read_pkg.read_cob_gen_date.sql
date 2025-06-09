create or replace function farms_read_pkg.read_cob_gen_date(
    in scenario_id farms.agristability_scenario.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select docs.generation_date
        from farms.benefit_calculation_document docs
        where docs.agristability_scenario_id = scenario_id;
    return cur;
end;
$$;
