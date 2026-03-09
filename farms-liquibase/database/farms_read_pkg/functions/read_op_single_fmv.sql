create or replace function farms_read_pkg.read_op_single_fmv(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
    in in_crop_unit_code farms.farm_fair_market_values.crop_unit_code%type
)
returns table(
    inventory_item_code     farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    crop_unit_code          farms.farm_reported_inventories.crop_unit_code%type,
    period                  farms.farm_fair_market_values.period%type,
    average_price           farms.farm_fair_market_values.average_price%type,
    percent_variance        farms.farm_fair_market_values.percent_variance%type,
    yearly_avg              farms.farm_fair_market_values.average_price%type
)
language sql
as $$
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
               avg_calc.yearly_avg,
               t.fiscal_start_year,
               t.fiscal_end_year,
               t.fiscal_start_month,
               t.fiscal_end_month,
               fmv.program_year fmv_year,
               fmv.period fmv_period
        from (
            select distinct extract(year from op.fiscal_year_start) fiscal_start_year,
                   extract(month from op.fiscal_year_start) fiscal_start_month,
                   extract(year from op.fiscal_year_end) fiscal_end_year,
                   extract(month from op.fiscal_year_end) fiscal_end_month,
                   (
                       (extract(year from op.fiscal_year_end) - extract(year from op.fiscal_year_start)) * 12 +
                       (extract(month from op.fiscal_year_end) - extract(month from op.fiscal_year_start)) + 1
                   ) fiscal_months,
                   pyv.municipality_code
            from farms.farm_farming_operations op
            join farms.farm_program_year_versions pyv on op.program_year_version_id = pyv.program_year_version_id
            where op.farming_operation_id = in_farming_operation_id
        ) t
        join farms.farm_fair_market_values fmv on fmv.inventory_item_code = in_inventory_item_code
                                            and fmv.crop_unit_code = in_crop_unit_code
                                            and (fmv.municipality_code = '0' or fmv.municipality_code = t.municipality_code)
                                            and (fmv.expiry_date is null or fmv.expiry_date > current_date)
        left join lateral (
            select avg(f2.average_price) as yearly_avg
            from farms.farm_fair_market_values f2
            where f2.inventory_item_code = fmv.inventory_item_code
            and f2.crop_unit_code = fmv.crop_unit_code
            and f2.municipality_code = fmv.municipality_code
            and (f2.expiry_date is null or f2.expiry_date > current_date)
            and (f2.program_year * 12 + f2.period) between
                (fmv.program_year * 12 + fmv.period - t.fiscal_months)
                and
                (fmv.program_year * 12 + fmv.period)
        ) avg_calc on true
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
$$;
