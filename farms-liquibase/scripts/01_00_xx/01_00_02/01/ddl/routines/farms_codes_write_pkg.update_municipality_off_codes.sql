create or replace procedure farms_codes_write_pkg.update_municipality_off_codes(
   in in_municipality_code farms.municipality_code.municipality_code%type,
   in office_codes varchar[],
   in in_user farms.municipality_code.update_user%type
)
language plpgsql
as $$
begin
    delete from farms.office_municipality_xref x
    where x.municipality_code = in_municipality_code;

    insert into farms.office_municipality_xref (
        office_municipality_xref_id,
        municipality_code,
        regional_office_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    )
    select nextval('farms.seq_omx'),
           in_municipality_code,
           r.regional_office_code,
           in_user,
           current_timestamp,
           in_user,
           current_timestamp,
           1
    from farms.regional_office r
    where r.regional_office_code = any(office_codes);

end;
$$;
