create or replace procedure farms_codes_write_pkg.create_municipality_code(
   in in_municipality_code farms.farm_municipality_codes.municipality_code%type,
   in in_description farms.farm_municipality_codes.description%type,
   in in_effective_date farms.farm_municipality_codes.established_date%type,
   in in_expiry_date farms.farm_municipality_codes.expiry_date%type,
   in office_codes varchar[],
   in in_user farms.farm_municipality_codes.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_municipality_codes (
        municipality_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    )
    values (
        in_municipality_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );

    call farms_codes_write_pkg.update_municipality_off_codes(
        in_municipality_code,
        office_codes,
        in_user
    );

    commit;
end;
$$;
