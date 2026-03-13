create or replace function farms_read_pkg.read_op_fmv_prev_year(
    in op_id farms.farm_farming_operations.farming_operation_id%type
)
returns table(
    inventory_item_code         farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    crop_unit_code              farms.farm_reported_inventories.crop_unit_code%type,
    prev_fy_end_average_price   farms.farm_fair_market_values.average_price%type
)
language sql
as $$
    select g.inventory_item_code,
           g.crop_unit_code,
           g.prev_fy_end_average_price
    from (
        select t.inventory_item_code,
               t.crop_unit_code,
               t.municipality_code,
               max(t.municipality_code) over (partition by t.inventory_item_code, t.crop_unit_code, t.program_year, t.period) mx_municipality_code,
               t.average_price prev_fy_end_average_price
        from (
            select distinct pyv.municipality_code,
                   x.inventory_item_code,
                   x.inventory_class_code,
                   (case when x.inventory_class_code = '1' then inv.crop_unit_code end) crop_unit_code,
                   fmv.average_price,
                   fmv.program_year,
                   fmv.period,
                   fmv.expiry_date
            from farms.farm_farming_operations op
            join farms.farm_program_year_versions pyv on op.program_year_version_id = pyv.program_year_version_id
            join farms.farm_reported_inventories inv on op.farming_operation_id = inv.farming_operation_id
            join farms.farm_agristabilty_cmmdty_xref x on inv.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id
            join farms.farm_fair_market_values fmv on fmv.inventory_item_code = x.inventory_item_code
                                                    -- either the crop code matches, or it's livestock
                                                    and ((x.inventory_class_code = '1' and fmv.crop_unit_code = inv.crop_unit_code) or x.inventory_class_code = '2')
                                                    and fmv.municipality_code in ('0', pyv.municipality_code)
                                                    and fmv.program_year = extract(year from op.fiscal_year_start - interval '1 month')
                                                    and fmv.period = extract(month from op.fiscal_year_start - interval '1 month')
            where op.farming_operation_id = op_id
        ) t
        where coalesce(t.expiry_date, '9999-12-31') > current_date
    ) g
    where g.municipality_code = mx_municipality_code
    order by g.inventory_item_code,
             g.crop_unit_code;
$$;
