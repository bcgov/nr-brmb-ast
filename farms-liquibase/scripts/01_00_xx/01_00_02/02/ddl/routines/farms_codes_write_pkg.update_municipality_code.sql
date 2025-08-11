create or replace procedure farms_codes_write_pkg.update_municipality_code(
   in in_municipality_code farms.farm_municipality_codes.municipality_code%type,
   in in_description farms.farm_municipality_codes.description%type,
   in in_effective_date farms.farm_municipality_codes.established_date%type,
   in in_expiry_date farms.farm_municipality_codes.expiry_date%type,
   in in_revision_count farms.farm_municipality_codes.revision_count%type,
   in office_codes varchar[],
   in in_user farms.farm_municipality_codes.who_updated%type
)
language plpgsql
as $$
begin

    call farms_codes_write_pkg.update_municipality_off_codes(
        in_municipality_code,
        office_codes,
        in_user
    );

    update farms.farm_municipality_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where municipality_code = in_municipality_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
