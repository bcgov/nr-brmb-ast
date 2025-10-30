create or replace function farms_webapp_pkg.get_puc_inventory_item_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select c.inventory_item_code code,
               c.description
        from farms.farm_inventory_item_codes c
        join farms.farm_agristabilty_cmmdty_xref x on x.inventory_item_code = c.inventory_item_code
        where current_timestamp between c.established_date and c.expiry_date
        and x.inventory_class_code in ('1','2')
        and c.inventory_item_code not in (
      	    select structure_group_code
            from farms.farm_structure_group_codes
            where current_timestamp between established_date and expiry_date
        )
        order by c.description asc;
    return v_cursor;
end;
$$;
