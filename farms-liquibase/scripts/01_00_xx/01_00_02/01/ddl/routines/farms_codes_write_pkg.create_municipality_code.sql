create or replace procedure farms_codes_write_pkg.create_municipality_code(
   in in_municipality_code farms.municipality_code.municipality_code%type,
   in in_description farms.municipality_code.description%type,
   in in_effective_date farms.municipality_code.effective_date%type,
   in in_expiry_date farms.municipality_code.expiry_date%type,
   in office_codes varchar[],
   in in_user farms.municipality_code.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.municipality_code (
        municipality_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
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
