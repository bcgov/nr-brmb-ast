create or replace function farms_codes_read_pkg.check_expected_production_item_exists(
    in in_inventory_code farms.farm_expected_productions.inventory_item_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.expected_production_id
        from farms.farm_expected_productions t
        where t.inventory_item_code = in_inventory_code;
    return cur;

end;
$$;
