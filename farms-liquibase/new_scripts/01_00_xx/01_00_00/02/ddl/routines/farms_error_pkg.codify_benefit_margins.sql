create or replace function farms_error_pkg.codify_benefit_margins(
    in msg varchar,
    in in_sc_id farms.farm_benefit_calc_totals.agristability_scenario_id%type
)
returns varchar
language plpgsql
as $$
begin
    -- no current FK constraints
    return farms_error_pkg.codify(msg);
end;
$$;
