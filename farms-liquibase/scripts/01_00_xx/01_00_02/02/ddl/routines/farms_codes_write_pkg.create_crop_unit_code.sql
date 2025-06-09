create or replace procedure farms_codes_write_pkg.create_crop_unit_code(
   in in_crop_unit_code farms.crop_unit_code.crop_unit_code%type,
   in in_description farms.crop_unit_code.description%type,
   in in_effective_date farms.crop_unit_code.effective_date%type,
   in in_expiry_date farms.crop_unit_code.expiry_date%type,
   in in_user farms.crop_unit_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.crop_unit_code(
        crop_unit_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        in_crop_unit_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
