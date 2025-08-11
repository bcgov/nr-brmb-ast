create or replace procedure farms_codes_write_pkg.update_municipality_off_codes(
   in in_municipality_code farms.farm_municipality_codes.municipality_code%type,
   in office_codes varchar[],
   in in_user farms.farm_municipality_codes.who_updated%type
)
language plpgsql
as $$
begin
    delete from farms.farm_office_municipality_xref x
    where x.municipality_code = in_municipality_code;

    insert into farms.farm_office_municipality_xref (
        office_municipality_xref_id,
        municipality_code,
        regional_office_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    )
    select nextval('farms.farm_omx_seq'),
           in_municipality_code,
           r.regional_office_code,
           in_user,
           current_timestamp,
           in_user,
           current_timestamp,
           1
    from farms.farm_regional_office_codes r
    where r.regional_office_code = any(office_codes);

end;
$$;
