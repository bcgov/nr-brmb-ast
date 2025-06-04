create or replace function farms_codes_read_pkg.export_missing_fmv(
    in in_program_year farms.fair_market_value.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        with inv as (
            select m.year,
                   m.municipality_code,
                   x.inventory_class_code,
                   x.inventory_item_code,
                   (case
                       when x.inventory_class_code = '2' then null
                       else ri.crop_unit_code
                   end) crop_unit_code
            from farms.operations_vw m
            join farms.reported_inventory ri on ri.farming_operation_id = m.farming_operation_id
            join farms.agristabilty_commodity_xref x on x.agristabilty_commodity_xref_id = ri.agristabilty_commodity_xref_id
            where x.inventory_class_code in ('1', '2')
            and x.inventory_item_code != '-1'
            and m.municipality_code not in ('-1', '0')
            and (ri.crop_unit_code is null or ri.crop_unit_code != '0' or x.inventory_class_code = '2')
            and (
                (x.inventory_class_code = '1' and ri.end_year_producer_price != 0) or
                ri.quantity_start != 0 or
                ri.quantity_end != 0 or
                ri.price_start != 0 or
                ri.price_end != 0
            )
            and m.year = in_program_year
            group by m.year,
                     m.municipality_code,
                     x.inventory_item_code,
                     x.inventory_class_code,
                     ri.crop_unit_code
        )
        select inv.year ||
               ',' || inv.municipality_code ||
               ',' || inv.inventory_item_code ||
               ',' || inv.crop_unit_code missing_fmv_export
        from inv
        where not exists (
            select null
            from farms.fair_market_value fmv
            where fmv.program_year = inv.year
            and fmv.inventory_item_code = inv.inventory_item_code
            and (fmv.municipality_code = inv.municipality_code or fmv.municipality_code = '0')
            and (fmv.crop_unit_code = inv.crop_unit_code or inv.inventory_class_code = '2')
        );
    return cur;
end;
$$;
