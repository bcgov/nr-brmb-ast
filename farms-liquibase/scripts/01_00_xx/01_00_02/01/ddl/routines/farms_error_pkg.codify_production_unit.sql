create or replace function farms_error_pkg.codify_production_unit(
    in msg varchar,
    in production_unit farms.crop_unit_code.crop_unit_code%type
)
returns varchar
language plpgsql
as $$
begin
    -- no current FK constraints
    return farms_error_pkg.codify(msg);
end;
$$;
