create or replace function farms_read_pkg.read_op_fmv_prev_year(
    in op_id farms.farm_farming_operations.farming_operation_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select g.inventory_item_code,
               g.crop_unit_code,
               g.prev_fy_end_average_price
        from (
            select prev_fy_fmv.inventory_item_code,
                   prev_fy_fmv.crop_unit_code,
                   prev_fy_fmv.municipality_code,
                   max(prev_fy_fmv.municipality_code) over (partition by prev_fy_fmv.inventory_item_code, prev_fy_fmv.crop_unit_code, prev_fy_fmv.program_year, prev_fy_fmv.period) mx_municipality_code,
                   prev_fy_fmv.average_price prev_fy_end_average_price
            from (
                select distinct to_number(to_char(op.fiscal_year_start - interval '1 month', 'YYYY')) prev_fiscal_end_year,
                       to_number(to_char(op.fiscal_year_start - interval '1 month', 'MM')) prev_fiscal_end_month,
                       pyv.municipality_code,
                       x.inventory_item_code,
                       x.inventory_class_code,
                       (case when x.inventory_class_code = '1' then inv.crop_unit_code end) crop_unit_code
                from farms.farm_farming_operations op
                join farms.farm_program_year_versions pyv on op.program_year_version_id = pyv.program_year_version_id
                join farms.farm_reported_inventories inv on op.farming_operation_id = inv.farming_operation_id
                join farms.farm_agristabilty_cmmdty_xref x on inv.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id
                where op.farming_operation_id = op_id
            ) t
            join farms.farm_fair_market_values prev_fy_fmv on prev_fy_fmv.inventory_item_code = t.inventory_item_code
                                                     -- either the crop code matches, or it's livestock
                                                     and ((t.inventory_class_code = '1' and prev_fy_fmv.crop_unit_code = t.crop_unit_code) or t.inventory_class_code = '2')
                                                     and (prev_fy_fmv.municipality_code = '0' or prev_fy_fmv.municipality_code = t.municipality_code)
                                                     and (prev_fy_fmv.expiry_date is null or prev_fy_fmv.expiry_date > current_date)
                                                     and prev_fy_fmv.program_year = t.prev_fiscal_end_year
                                                     and prev_fy_fmv.period = t.prev_fiscal_end_month
            where prev_fy_fmv.program_year = prev_fiscal_end_year
        ) g
        where g.municipality_code = mx_municipality_code
        order by g.inventory_item_code,
                 g.crop_unit_code;

    return cur;
end;
$$;
