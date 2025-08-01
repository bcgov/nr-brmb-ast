create or replace procedure farms_codes_write_pkg.delete_fmv(
   in in_program_year farms.fair_market_value.program_year%type,
   in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
   in in_municipality_code farms.fair_market_value.municipality_code%type,
   in in_crop_unit_code farms.fair_market_value.crop_unit_code%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin
    v_in_use := farms_codes_write_pkg.in_use_fmv(
        in_program_year,
        in_inventory_item_code,
        in_municipality_code,
        in_crop_unit_code
    );

    if v_in_use = 0 then
        delete from farms.fair_market_value
        where program_year = in_program_year
          and inventory_item_code = in_inventory_item_code
          and municipality_code = in_municipality_code
          and crop_unit_code = in_crop_unit_code;
    end if;
end;
$$;
