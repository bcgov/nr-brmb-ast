create or replace function farms_error_pkg.codify_operational_partner(
    in msg varchar,
    in in_farming_operation_id farms.farm_farming_operatin_prtnrs.farming_operation_id%type
)
returns varchar
language plpgsql
as $$
begin
    -- no current FK constraints
    return farms_error_pkg.codify(msg);
end;
$$;
