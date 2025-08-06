create or replace function farms_import_pkg.is_bpu_set_complete(
    in in_program_year_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
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
                from farms.farm_agristability_scenarios s
                where s.agristability_scenario_id = any(scenario_ids)
            ) s
            join farms.farm_agristability_scenarios ps on ps.agristability_scenario_id = in_program_year_scenario_id
            join farms.farm_program_year_versions ppyv on ppyv.program_year_version_id = ps.program_year_version_id
            join farms.farm_program_years ppy on ppy.program_year_id = ppyv.program_year_id
            join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
            join farms.farm_farming_operations fo on fo.program_year_version_id = ppyv.program_year_version_id
            join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
                                                    and puc.agristability_scenario_id is null
            where puc.productive_capacity_amount != 0
            and not exists (
                select null
                from farms.farm_benchmark_per_units bpu
                join farms.farm_benchmark_years bnch on bnch.benchmark_per_unit_id = bpu.benchmark_per_unit_id
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
