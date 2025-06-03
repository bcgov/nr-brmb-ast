create or replace procedure farms_codes_write_pkg.delete_sector_detail_code(
   in in_sector_detail_code farms.sector_detail_code.sector_detail_code%type,
   in in_revision_count farms.sector_detail_code.revision_count%type
)
language plpgsql
as $$
begin

    delete from farms.sector_detail_xref sdx
    where sdx.sector_detail_code = in_sector_detail_code;

    delete from farms.sector_detail_code c
    where c.sector_detail_code = in_sector_detail_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
