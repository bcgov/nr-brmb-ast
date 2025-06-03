create or replace function farms_codes_write_pkg.in_use_sector_detail_code(
    in in_sector_detail_code farms.sector_detail_code.sector_detail_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    select (case
        when exists(
            select null
            from farms.sector_detail_line_item t
            where t.sector_detail_code = in_sector_detail_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
