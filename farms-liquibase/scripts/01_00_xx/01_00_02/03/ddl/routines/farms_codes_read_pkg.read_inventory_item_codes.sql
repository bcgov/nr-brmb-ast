create or replace function farms_codes_read_pkg.read_inventory_item_codes(
    in in_code farms.inventory_item_code.inventory_item_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select c.inventory_item_code code,
               c.description,
               a.rollup_inventory_item_code,
               (
                   select description
                   from farms.inventory_item_code
                   where inventory_item_code = a.rollup_inventory_item_code
                   limit 1
               ) rollup_inventory_item_desc,
               c.effective_date,
               c.expiry_date,
               c.revision_count
        from farms.inventory_item_code c
        left join farms.inventory_item_attribute a on c.inventory_item_code = a.inventory_item_code
        where (in_code is null or c.inventory_item_code = in_code)
        order by lower(c.description);
    return cur;
end;
$$;
