create or replace procedure farms_codes_write_pkg.update_sector_detail_code(
   in in_sector_code farms.farm_sector_codes.sector_code%type,
   in in_sector_detail_code farms.farm_sector_detail_codes.sector_detail_code%type,
   in in_description farms.farm_sector_detail_codes.description%type,
   in in_expiry_date farms.farm_sector_detail_codes.expiry_date%type,
   in in_revision_count farms.farm_sector_detail_codes.revision_count%type,
   in in_user farms.farm_sector_detail_codes.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_sector_detail_xref sdx
    set sdx.sector_code = in_sector_code,
        sdx.revision_count = sdx.revision_count + 1,
        sdx.who_updated = in_user,
        sdx.when_updated = current_timestamp
    where sdx.sector_detail_code = in_sector_detail_code;

    update farms.farm_sector_detail_codes c
    set c.description = in_description,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.who_updated = in_user,
        c.when_updated = current_timestamp
    where c.sector_detail_code = in_sector_detail_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
