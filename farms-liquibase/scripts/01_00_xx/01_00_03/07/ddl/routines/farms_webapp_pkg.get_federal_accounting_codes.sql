create or replace function farms_webapp_pkg.get_federal_accounting_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select federal_accounting_code code,
               federal_accounting_code || ' - ' || description description
        from farms.farm_federal_accounting_codes
        where current_timestamp between established_date and expiry_date
        order by description asc;
    return v_cursor;
end;
$$;
