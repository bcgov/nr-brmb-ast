create or replace procedure farms_codes_write_pkg.update_crop_unit_code(
   in in_crop_unit_code farms.crop_unit_code.crop_unit_code%type,
   in in_description farms.crop_unit_code.description%type,
   in in_effective_date farms.crop_unit_code.effective_date%type,
   in in_expiry_date farms.crop_unit_code.expiry_date%type,
   in in_revision_count farms.crop_unit_code.revision_count%type,
   in in_user farms.crop_unit_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.crop_unit_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.crop_unit_code = in_crop_unit_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
