create or replace function farms_import_pkg.is_fmv_set_complete(
    in scenario_ids numeric[]
)
returns varchar
language plpgsql
as $$
declare
    fmv_set_complete_ind varchar;
begin
    -- For CRA scenario so do not check adjustments
    select (case
        when exists(
            select null
            from (
                select trunc(fo.fiscal_year_start, 'MM') as fiscal_year_start,
                       trunc(fo.fiscal_year_end, 'MM') as fiscal_year_end,
                       fo.fiscal_months,
                       pyv.municipality_code,
                       x.inventory_item_code,
                       (case
                           when x.inventory_class_code = '2' then farms_codes_read_pkg.get_livestock_unit_code(x.inventory_item_code)
                           else ri.crop_unit_code
                       end) crop_unit_code
                from (
                    select s.program_year_version_id
                    from farms.farm_agristability_scenarios s
                    where s.agristability_scenario_id = any(scenario_ids)
                ) s
                join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
                join (
                    select fo.*,
                           (
                               (to_number(to_char(fo.fiscal_year_end, 'YYYY')) - to_number(to_char(fo.fiscal_year_start, 'YYYY'))) * 12 +
                               (to_number(to_char(fo.fiscal_year_end, 'MM')) - to_number(to_char(fo.fiscal_year_start, 'MM'))) + 1
                           ) fiscal_months
                    from farms.farm_farming_operations fo
                ) fo on fo.program_year_version_id = pyv.program_year_version_id
                join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
                                                 and ri.agristability_scenario_id is null
                join farms.farm_agristabilty_cmmdty_xref x on x.agristabilty_cmmdty_xref_id = ri.agristabilty_cmmdty_xref_id
                where x.inventory_class_code in ('1', '2')
                and (
                    (x.inventory_class_code = '1' and ri.quantity_produced != 0) or
                    ri.quantity_start != 0 or
                    ri.quantity_end != 0 or
                    ri.price_start != 0 or
                    ri.price_end != 0
                )
            ) t
            where t.fiscal_months != (
                select count(1)
                from farms.farm_fair_market_values fmv
                where fmv.inventory_item_code = t.inventory_item_code
                and fmv.crop_unit_code = t.crop_unit_code
                and (fmv.expiry_date is null or fmv.expiry_date > current_date)
                and fmv.municipality_code = t.municipality_code
                and to_date(fmv.program_year||'/'||fmv.period, 'YYYY/MM') between fiscal_start and fiscal_end
            )
            and f.fiscal_months != (
                select count(1)
                from farms.farm_fair_market_values fmv
                where fmv.inventory_item_code = t.inventory_item_code
                and fmv.crop_unit_code = t.crop_unit_code
                and (fmv.expiry_date is null or fmv.expiry_date > current_date)
                and fmv.municipality_code = '0'
                and to_date(fmv.program_year||'/'||fmv.period, 'YYYY/MM') between fiscal_start and fiscal_end
            )
        ) then 'N'
        else 'Y'
    end) into fmv_set_complete_ind;

    return fmv_set_complete_ind;
end;
$$;
