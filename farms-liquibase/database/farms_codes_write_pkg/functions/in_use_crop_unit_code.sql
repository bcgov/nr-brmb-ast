create or replace function farms_codes_write_pkg.in_use_crop_unit_code(
    in in_crop_unit_code farms.farm_crop_unit_codes.crop_unit_code%type
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
            from farms.farm_reported_inventories t
            where t.crop_unit_code = in_crop_unit_code
        )
        or exists(
            select null
            from farms.farm_fair_market_values t
            where t.crop_unit_code = in_crop_unit_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
