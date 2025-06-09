create or replace function farms_error_pkg.codify_claim(
    in msg varchar,
    in in_sc_id farms.agristability_claim.agristability_scenario_id%type
)
returns varchar
language plpgsql
as $$
begin
    -- no current FK constraints
    return farms_error_pkg.codify(msg);
end;
$$;
