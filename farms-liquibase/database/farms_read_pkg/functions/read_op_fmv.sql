create or replace function farms_read_pkg.read_op_fmv(
    in op_id farms.farm_farming_operations.farming_operation_id%type
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
    with fmv as (
        select distinct extract(year from op.fiscal_year_start) fiscal_start_year,
               extract(month from op.fiscal_year_start) fiscal_start_month,
               extract(year from op.fiscal_year_end) fiscal_end_year,
               extract(month from op.fiscal_year_end) fiscal_end_month,
               (
                   (extract(year from op.fiscal_year_end) - extract(year from op.fiscal_year_start)) * 12 +
                   (extract(month from op.fiscal_year_end) - extract(month from op.fiscal_year_start)) + 1
               ) fiscal_months,
               x.inventory_class_code,
               fmv.fair_market_value_id,
               fmv.program_year,
               fmv.inventory_item_code,
               fmv.crop_unit_code,
               fmv.period,
               fmv.average_price,
               fmv.percent_variance,
               fmv.municipality_code,
               max(fmv.municipality_code) over (partition by fmv.inventory_item_code, fmv.crop_unit_code, fmv.program_year, fmv.period) mx_municipality_code,
               fmv.expiry_date
        from farms.farm_farming_operations op
        join farms.farm_program_year_versions pyv on op.program_year_version_id = pyv.program_year_version_id
        join farms.farm_reported_inventories inv on op.farming_operation_id = inv.farming_operation_id
        join farms.farm_agristabilty_cmmdty_xref x on inv.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id
        join farms.farm_fair_market_values fmv on fmv.inventory_item_code = x.inventory_item_code
                                            and (
                                                (x.inventory_class_code = '1' and fmv.crop_unit_code = inv.crop_unit_code)
                                                or x.inventory_class_code = '2'
                                            )
                                            and fmv.municipality_code in ('0', pyv.municipality_code)
                                            and coalesce(fmv.expiry_date, '9999-12-31') > current_date
                                            and fmv.program_year between extract(year from op.fiscal_year_start) and extract(year from op.fiscal_year_end)
        where op.farming_operation_id = op_id
    ), g as (
        select f1.fair_market_value_id,
               f1.program_year,
               f1.inventory_item_code,
               f1.crop_unit_code,
               f1.period,
               f1.average_price,
               f1.percent_variance,
               f1.municipality_code,
               f1.mx_municipality_code,
               avg_calc.yearly_avg,
               f1.fiscal_start_year,
               f1.fiscal_end_year,
               f1.fiscal_start_month,
               f1.fiscal_end_month
        from fmv f1
        left join lateral (
            select avg(f2.average_price) as yearly_avg
            from fmv f2
            where f2.inventory_item_code = f1.inventory_item_code
            and f2.crop_unit_code = f1.crop_unit_code
            and f2.municipality_code = f1.municipality_code
            and (f2.program_year * 12 + f2.period) between
                (f1.program_year * 12 + f1.period - f1.fiscal_months)
                and
                (f1.program_year * 12 + f1.period)
        ) avg_calc on true
    )
    select g.inventory_item_code,
           g.crop_unit_code,
           g.period,
           g.average_price,
           g.percent_variance,
           (case
               when (g.program_year*12+g.period) = max(g.program_year*12+g.period) over (partition by g.inventory_item_code, g.crop_unit_code) then g.yearly_avg
               else null
           end) yearly_avg
    from g
    where g.municipality_code = g.mx_municipality_code
    and (
        (g.fiscal_start_year = g.program_year and g.fiscal_start_month = g.period) or
        (g.fiscal_end_year = g.program_year and g.fiscal_end_month = g.period)
    )
    order by g.inventory_item_code,
             g.crop_unit_code,
             g.program_year,
             g.period;
$$;
