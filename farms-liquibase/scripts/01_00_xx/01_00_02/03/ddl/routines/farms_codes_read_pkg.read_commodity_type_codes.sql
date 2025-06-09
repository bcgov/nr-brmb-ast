create or replace function farms_codes_read_pkg.read_commodity_type_codes()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select ctc.commodity_type_code,
               ctc.description
        from farms.commodity_type_code ctc;
    return cur;

end;
$$;
