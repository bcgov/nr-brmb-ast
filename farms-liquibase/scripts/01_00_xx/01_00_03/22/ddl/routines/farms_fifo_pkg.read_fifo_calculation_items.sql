create or replace function farms_fifo_pkg.read_fifo_calculation_items()
returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        with scenarios as (
            select sv.participant_pin,
                   sv.year program_year,
                   sv.program_year_version_id cra_pyv_id,
                   sv.agristability_scenario_id,
                   first_value(sv.agristability_scenario_id) over (
                        partition by sv.program_year_id
                        order by case when sv.scenario_class_code = 'CRA' then 0 else 1 end,  -- Get CRA scenario.
                                 sv.program_year_version_number desc,                         -- Get latest CRA data.
                                 sv.scenario_number desc  -- Should be redundant. Should not be two with the same PYV number.
                   ) cra_scenario_id,
                   sv.scenario_number cra_scenario_number,
                   first_value(case when sv.scenario_class_code = 'FIFO' then sv.agristability_scenario_id else null end) over (
                        partition by sv.program_year_id
                        order by case when sv.scenario_class_code = 'FIFO' then 0 else 1 end, -- Get the FAILED FIFO scenario first.
                                           sv.scenario_number desc  -- Should be redundant. There should not be two FIFO scenarios.
                   ) fifo_scenario_id
            from farms.farm_scenarios_vw sv
            where sv.year in (((to_char(current_timestamp, 'YYYY'))::numeric  - 1), (to_char(current_timestamp, 'YYYY'))::numeric )
            and (
                sv.scenario_class_code = 'CRA'
                or (
                    sv.scenario_class_code = 'FIFO'
                    and sv.scenario_state_code in ('IP', 'FAILED')
                )
            )
            and exists (
                select * from
                farms.farm_scenarios_vw sv2
                where sv2.program_year_id = sv.program_year_id
                and sv2.scenario_class_code = 'CRA'
            )
            and not exists (
                select *
                from farms.farm_scenarios_vw sv3
                where sv3.program_year_id = sv.program_year_id
                and (
                    -- Skip if there is an existing USER scenario (ignore comparison scenarios).
                    (sv3.scenario_class_code = 'USER' and sv3.scenario_category_code not in ('CS','UNK'))
                    -- Skip if FIFO calculation has alread been compelted.
                    or (sv3.scenario_class_code = 'FIFO' and sv3.scenario_state_code = 'COMPLETED')
                )
            )
        )
        select scenarios.participant_pin,
               scenarios.program_year,
               scenarios.cra_pyv_id,
               scenarios.cra_scenario_id,
               scenarios.cra_scenario_number,
               scenarios.fifo_scenario_id,
               sv_fifo.scenario_number fifo_scenario_number,
               sv_fifo.program_year_version_id fifo_pyv_id,
               sv_fifo.scenario_state_code fifo_scenario_state_code
        from scenarios
        left outer join farms.farm_scenarios_vw sv_fifo on sv_fifo.agristability_scenario_id = scenarios.fifo_scenario_id
        where scenarios.agristability_scenario_id = scenarios.cra_scenario_id
        and (
            exists (
                select *
                from farms.farm_farming_operations fo
                join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
                where fo.program_year_version_id = scenarios.cra_pyv_id
            )
            or exists (
                select *
                from farms.farm_farming_operations fo
                join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
                join farms.farm_agristabilty_cmmdty_xref acx on acx.agristabilty_cmmdty_xref_id = ri.agristabilty_cmmdty_xref_id
                where fo.program_year_version_id = scenarios.cra_pyv_id
            )
        )
        order by scenarios.program_year,
              scenarios.participant_pin;

    return cur;
end;
$$;
