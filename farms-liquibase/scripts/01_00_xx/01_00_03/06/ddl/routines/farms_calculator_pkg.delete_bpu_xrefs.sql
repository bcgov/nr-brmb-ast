create or replace procedure farms_calculator_pkg.delete_bpu_xrefs(
    in in_scenario_id farms.farm_scenario_bpu_xref.agristability_scenario_id%type
)
language plpgsql
as
$$
begin
    delete from farms.farm_scenario_bpu_xref
    where agristability_scenario_id = in_scenario_id;
end;
$$;
