create or replace function farms_import_pkg.is_bpu_set_complete(
    in in_program_year_scenario_id farms.agristability_scenario.agristability_scenario_id%type,
    in scenario_ids numeric[]
)
returns varchar
language plpgsql
as $$
declare
    bpu_set_complete_ind varchar;
begin
    -- For CRA scenario so do not check adjustments
    select (case
        when exists(
            select null
            from (
                select s.program_year_version_id
                from farms.agristability_scenario s
                where s.agristability_scenario_id = any(scenario_ids)
            ) s
            join farms.agristability_scenario ps on ps.agristability_scenario_id = in_program_year_scenario_id
            join farms.program_year_version ppyv on ppyv.program_year_version_id = ps.program_year_version_id
            join farms.program_year ppy on ppy.program_year_id = ppyv.program_year_id
            join farms.program_year_version pyv on pyv.program_year_version_id = s.program_year_version_id
            join farms.farming_operation fo on fo.program_year_version_id = ppyv.program_year_version_id
            join farms.productive_unit_capacity puc on puc.farming_operation_id = fo.farming_operation_id
                                                    and puc.agristability_scenario_id is null
            where puc.productive_capacity_amount != 0
            and not exists (
                select null
                from farms.benchmark_per_unit bpu
                join farms.benchmark_year bnch on bnch.benchmark_per_unit_id = bpu.benchmark_per_unit_id
                where bpu.program_year = ppy.year
                and (bpu.municipality_code = ppyv.municipality_code
                     or bpu.municipality_code = '0')
                and (bpu.inventory_item_code = puc.inventory_item_code
                     or bpu.inventory_group_code = puc.inventory_group_code)
            )
        ) then 'N'
        else 'Y'
    end) into bpu_set_complete_ind;

    return bpu_set_complete_ind;
end;
$$;
