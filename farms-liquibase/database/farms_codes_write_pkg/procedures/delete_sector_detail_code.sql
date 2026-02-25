create or replace procedure farms_codes_write_pkg.delete_sector_detail_code(
   in in_sector_detail_code farms.farm_sector_detail_codes.sector_detail_code%type,
   in in_revision_count farms.farm_sector_detail_codes.revision_count%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
begin

    delete from farms.farm_sector_detail_xref sdx
    where sdx.sector_detail_code = in_sector_detail_code;

    delete from farms.farm_sector_detail_codes c
    where c.sector_detail_code = in_sector_detail_code
    and c.revision_count = in_revision_count;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
