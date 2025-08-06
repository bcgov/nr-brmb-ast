create or replace procedure farms_codes_write_pkg.update_crop_unit_code(
   in in_crop_unit_code farms.farm_crop_unit_codes.crop_unit_code%type,
   in in_description farms.farm_crop_unit_codes.description%type,
   in in_effective_date farms.farm_crop_unit_codes.established_date%type,
   in in_expiry_date farms.farm_crop_unit_codes.expiry_date%type,
   in in_revision_count farms.farm_crop_unit_codes.revision_count%type,
   in in_user farms.farm_crop_unit_codes.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_crop_unit_codes c
    set c.description = in_description,
        c.established_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.who_updated = in_user,
        c.when_updated = current_timestamp
    where c.crop_unit_code = in_crop_unit_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
