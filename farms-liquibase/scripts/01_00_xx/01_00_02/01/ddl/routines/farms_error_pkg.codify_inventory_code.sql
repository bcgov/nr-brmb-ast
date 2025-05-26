create or replace function farms_error_pkg.codify_inventory_code(
    in msg varchar,
    in inventory_code varchar,
    in expiry_date date
)
returns varchar
language plpgsql
as $$
begin
    -- no current FK constraints
    return farms_error_pkg.codify(msg);
end;
$$;
