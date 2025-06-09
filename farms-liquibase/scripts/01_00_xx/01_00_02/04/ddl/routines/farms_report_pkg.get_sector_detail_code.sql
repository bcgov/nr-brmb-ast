create or replace function farms_report_pkg.get_sector_detail_code(
    in in_agristability_scenario_id farms.agristability_scenario.agristability_scenario_id%type
)
returns varchar
language plpgsql
as $$
declare
    sector_detail_code_cursor(p_scenario_ids numeric[]) cursor for
        select (case
                   when t.sector_detail_code is null or t.sector_detail_income_percent = 0 then 'NO_INCOME'
                   when t.sector_detail_income_percent >= 50 then t.sector_detail_code
                   when t.sector_income_percent >= 50 then mix.sector_detail_code
                   else 'MIX_FARM'
               end) sector_detail_code
        from (select 1) dual
        left outer join (
            select sector_code,
                   sector_detail_code,
                   (case when total_income = 0 then 0 else trunc(sector_income_sum / total_income * 100, 0) end) sector_income_percent,
                   trunc(sector_detail_income_sum / sum(sector_detail_income_sum) over (partition by null) * 100, 0) sector_detail_income_percent,
                   rank() over (order by sector_detail_income_sum desc, sector_detail_code) income_rank,
                   total_income,
                   sector_income_sum,
                   sector_detail_income_sum
            from (
                select sec.sector_code,
                       sdc.sector_detail_code,
                       sum(rie.amount) over (partition by null) total_income,
                       sum(rie.amount) over (partition by sec.sector_code) sector_income_sum,
                       sum(rie.amount) over (partition by sec.sector_code, sdc.sector_detail_code) sector_detail_income_sum,
                       rank() over (partition by sec.sector_code, sdc.sector_detail_code order by rie.reported_income_expense_id) ie_rnk
                from farms.agristability_scenario sc
                join farms.program_year_version pyv on pyv.program_year_version_id = sc.program_year_version_id
                join farms.program_year py on py.program_year_id = pyv.program_year_id
                join farms.farming_operation fo on fo.program_year_version_id = pyv.program_year_version_id
                join farms.reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                        and (rie.agristability_scenario_id = sc.agristability_scenario_id or rie.agristability_scenario_id is null)
                join farms.line_item li on li.line_item = rie.line_item
                                        and li.program_year = py.year
                left outer join farms.sector_detail_line_item sdli on sdli.line_item = li.line_item
                                                                   and sdli.program_year = li.program_year
                left outer join farms.sector_detail_code sdc on sdc.sector_detail_code = sdli.sector_detail_code
                left outer join farms.sector_detail_xref sdx on sdx.sector_detail_code = sdc.sector_detail_code
                left outer join farms.sector_code sec on sec.sector_code = sdx.sector_code
                where sc.agristability_scenario_id = any(p_scenario_ids)
                and li.eligibility_indicator = 'Y'
                and rie.expense_indicator = 'N'
                and li.exclude_from_revenue_calculation_indicator = 'N'
                and current_date between li.established_date and li.expiry_date
            )
            where ie_rnk = 1
            and sector_detail_income_sum != 0
        ) t on 1 = 1
        left outer join farms.sector_detail_xref mix on mix.sector_code = t.sector_code
                                                     and mix.sector_detail_code in ('MIX_CATTLE', 'MIX_CR_G_O', 'MIX_FRUIT', 'MIX_LIVE', 'MIX_OTH_CR', 'MIX_SM',   'MIX_VEG');
        where (t.income_rank = 1 or t.income_rank is null);

    v_sector_detail_code farms.sector_code.description%type;
    v_scenario_ids numeric[] := null;
    v_combined_farm_number farms.agristability_scenario.combined_farm_number%type;
begin

    select s.combined_farm_number
    into v_combined_farm_number
    from farms.agristability_scenario s
    where s.agristability_scenario_id = in_agristability_scenario_id;

    if combined_farm_number is null then
        v_scenario_ids := array[in_agristability_scenario_id];
    else
        select array_agg(s.agristability_scenario_id)
        into v_scenario_ids
        from farms.agristability_scenario s
        where s.combined_farm_number = v_combined_farm_number;
    end if;

    open sector_detail_code_cursor(v_scenario_ids);
    fetch sector_detail_code_cursor into v_sector_detail_code;
    close sector_detail_code_cursor;

    return v_sector_detail_code;

end;
$$;
