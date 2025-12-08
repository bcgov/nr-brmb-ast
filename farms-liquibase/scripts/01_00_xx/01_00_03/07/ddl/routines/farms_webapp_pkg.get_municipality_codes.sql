create or replace function farms_webapp_pkg.get_municipality_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select municipality_code code,
               description
        from farms.farm_municipality_codes
        where current_timestamp between established_date and expiry_date
        order by description asc;
    return v_cursor;
end;
$$;
