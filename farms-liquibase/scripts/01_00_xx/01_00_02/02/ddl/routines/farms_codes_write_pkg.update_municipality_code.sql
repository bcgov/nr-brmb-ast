create or replace procedure farms_codes_write_pkg.update_municipality_code(
   in in_municipality_code farms.municipality_code.municipality_code%type,
   in in_description farms.municipality_code.description%type,
   in in_effective_date farms.municipality_code.effective_date%type,
   in in_expiry_date farms.municipality_code.expiry_date%type,
   in in_revision_count farms.municipality_code.revision_count%type,
   in office_codes varchar[],
   in in_user farms.municipality_code.update_user%type
)
language plpgsql
as $$
begin

    call farms_codes_write_pkg.update_municipality_off_codes(
        in_municipality_code,
        office_codes,
        in_user
    );

    update farms.municipality_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.municipality_code = in_municipality_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
