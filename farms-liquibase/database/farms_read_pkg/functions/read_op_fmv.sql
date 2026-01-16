create or replace function farms_read_pkg.read_op_fmv(
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
               g.period,
               g.average_price,
               g.percent_variance,
               (case
                   when (g.program_year*12+g.period) = max(g.program_year*12+g.period) over (partition by g.inventory_item_code, g.crop_unit_code) then g.yearly_avg
                   else null
               end) yearly_avg
        from (
            select fmv.fair_market_value_id,
                   fmv.program_year,
                   fmv.inventory_item_code,
                   fmv.crop_unit_code,
                   fmv.period,
                   fmv.average_price,
                   fmv.percent_variance,
                   fmv.municipality_code,
                   max(fmv.municipality_code) over (partition by fmv.inventory_item_code, fmv.crop_unit_code, fmv.program_year, fmv.period) mx_municipality_code,
                   avg(fmv.average_price) over (
                       partition by fmv.inventory_item_code, fmv.crop_unit_code, fmv.municipality_code
                       order by fmv.program_year, fmv.period
                       rows between t.fiscal_months preceding and current row
                   ) yearly_avg,
                   t.fiscal_start_year,
                   t.fiscal_end_year,
                   t.fiscal_start_month,
                   t.fiscal_end_month,
                   fmv.program_year fmv_year,
                   fmv.period fmv_period
            from (
                select distinct to_number(to_char(op.fiscal_year_start, 'YYYY')) fiscal_start_year,
                       to_number(to_char(op.fiscal_year_start, 'MM')) fiscal_start_month,
                       to_number(to_char(op.fiscal_year_end, 'YYYY')) fiscal_end_year,
                       to_number(to_char(op.fiscal_year_end, 'MM')) fiscal_end_month,
                       (
                           (to_number(to_char(op.fiscal_year_end, 'YYYY')) - to_number(to_char(op.fiscal_year_start, 'YYYY'))) * 12 +
                           (to_number(to_char(op.fiscal_year_end, 'MM')) - to_number(to_char(op.fiscal_year_start, 'MM'))) + 1
                       ) fiscal_months,
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
            join farms.farm_fair_market_values fmv on fmv.inventory_item_code = t.inventory_item_code
                                             -- either the crop code matches, or it's livestock
                                             and ((t.inventory_class_code = '1' and fmv.crop_unit_code = t.crop_unit_code) or t.inventory_class_code = '2')
                                             and (fmv.municipality_code = '0' or fmv.municipality_code = t.municipality_code)
                                             and (fmv.expiry_date is null or fmv.expiry_date > current_date)
            -- check fiscal year window
            where fmv.program_year between t.fiscal_start_year and t.fiscal_end_year
        ) g
        where g.municipality_code = mx_municipality_code
        and (
            (g.fiscal_start_year = g.fmv_year and g.fiscal_start_month = g.fmv_period) or
            (g.fiscal_end_year = g.fmv_year and g.fiscal_end_month = g.fmv_period)
        )
        order by g.inventory_item_code,
                 g.crop_unit_code,
                 g.program_year,
                 g.period;

    return cur;
end;
$$;
