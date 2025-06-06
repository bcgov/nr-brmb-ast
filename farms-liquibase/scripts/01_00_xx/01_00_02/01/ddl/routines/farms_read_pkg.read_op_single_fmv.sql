create or replace function farms_read_pkg.read_op_single_fmv(
    in in_farming_operation_id farms.farming_operation.farming_operation_id%type,
    in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
    in in_crop_unit_code farms.fair_market_value.crop_unit_code%type
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
                       pyv.municipality_code
                from farms.farming_operation op
                join farms.program_year_version pyv on op.program_year_version_id = pyv.program_year_version_id
                where op.farming_operation_id = in_farming_operation_id
            ) t
            join farms.fair_market_value fmv on fmv.inventory_item_code = in_inventory_item_code
                                             and fmv.crop_unit_code = in_crop_unit_code
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
