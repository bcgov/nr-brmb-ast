create or replace function farms_codes_read_pkg.read_inventory_item_details(
    in in_code farms.inventory_item_code.inventory_item_code%type,
    in in_year farms.inventory_item_detail.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select iid.inventory_item_detail_id,
               iid.inventory_item_code,
               iid.insurable_value,
               iid.premium_rate,
               iid.program_year,
               iid.eligibility_indicator,
               iid.fruit_vegetable_type_code,
               iid.line_item,
               iid.commodity_type_code,
               iid.revision_count
        from farms.inventory_item_detail iid
        where (in_code is null or iid.inventory_item_code = in_code)
          and (in_year is null or iid.program_year = in_year);
    return cur;
end;
$$;
