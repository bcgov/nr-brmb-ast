create or replace function farms_codes_write_pkg.in_use_fmv(
    in in_program_year farms.fair_market_value.program_year%type,
    in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
    in in_municipality_code farms.fair_market_value.municipality_code%type,
    in in_crop_unit_code farms.fair_market_value.crop_unit_code%type
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
            from farms.program_year_version pyv
            join farms.farming_operation fo on fo.program_year_version_id = pyv.program_year_version_id
            join farms.reported_inventory ri on ri.farming_operation_id = fo.farming_operation_id
            join farms.agristabilty_commodity_xref x on x.agristabilty_commodity_xref_id = ri.agristabilty_commodity_xref_id
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
