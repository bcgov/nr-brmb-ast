create or replace procedure farms_codes_write_pkg.update_sector_code(
   in in_sector_code farms.sector_code.sector_code%type,
   in in_description farms.sector_code.description%type,
   in in_expiry_date farms.sector_code.expiry_date%type,
   in in_revision_count farms.sector_code.revision_count%type,
   in in_user farms.sector_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.sector_code c
    set c.description = in_description,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_timestamp = current_timestamp
    where c.sector_code = in_sector_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
