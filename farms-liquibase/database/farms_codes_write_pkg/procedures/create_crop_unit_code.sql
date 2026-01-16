create or replace procedure farms_codes_write_pkg.create_crop_unit_code(
   in in_crop_unit_code farms.farm_crop_unit_codes.crop_unit_code%type,
   in in_description farms.farm_crop_unit_codes.description%type,
   in in_effective_date farms.farm_crop_unit_codes.established_date%type,
   in in_expiry_date farms.farm_crop_unit_codes.expiry_date%type,
   in in_user farms.farm_crop_unit_codes.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_crop_unit_codes(
        crop_unit_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
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
