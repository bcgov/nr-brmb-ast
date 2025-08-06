create or replace function farms_codes_write_pkg.in_use_fmv(
    in in_program_year farms.farm_fair_market_values.program_year%type,
    in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
    in in_municipality_code farms.farm_fair_market_values.municipality_code%type,
    in in_crop_unit_code farms.farm_fair_market_values.crop_unit_code%type
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
            from farms.farm_program_year_versions pyv
            join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
            join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
            join farms.farm_agristabilty_cmmdty_xref x on x.agristabilty_cmmdty_xref_id = ri.agristabilty_cmmdty_xref_id
            where pyv.municipality_code = in_municipality_code
            and (to_number(to_char(fo.fiscal_year_start, 'YYYY')) = in_program_year
                 or to_number(to_char(fo.fiscal_year_end, 'YYYY')) = in_program_year)
            and x.inventory_item_code = in_inventory_item_code
            and x.inventory_class_code in ('1', '2')
            and (x.inventory_class_code = '2' or ri.crop_unit_code = in_crop_unit_code)
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
