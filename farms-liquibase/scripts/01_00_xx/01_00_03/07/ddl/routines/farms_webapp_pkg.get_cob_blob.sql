create or replace function farms_webapp_pkg.get_cob_blob(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select docs.document
        from farms.farm_benefit_calc_documents docs
        where docs.agristability_scenario_id = in_scenario_id;
    return v_cursor;
end;
$$;
