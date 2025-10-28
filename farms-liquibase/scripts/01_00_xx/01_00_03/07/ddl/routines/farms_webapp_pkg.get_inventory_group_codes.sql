create or replace function farms_webapp_pkg.get_inventory_group_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select inventory_group_code code,
               description
        from farms.farm_inventory_group_codes
        where current_timestamp between established_date and expiry_date
        order by description asc;
    return v_cursor;
end;
$$;
