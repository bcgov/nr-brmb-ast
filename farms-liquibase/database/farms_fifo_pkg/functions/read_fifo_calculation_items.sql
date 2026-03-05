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
                        order by sv.program_year_version_number desc,                         -- Get latest CRA data.
                                 sv.scenario_number desc  -- Should be redundant. Should not be two with the same PYV number.
                   ) cra_scenario_id,
                   sv.scenario_number cra_scenario_number
            from farms.farm_scenarios_vw sv
            join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = sv.agristability_scenario_id
            join farms.farm_program_years py on py.program_year_id = sv.program_year_id
            where sv.year in (((to_char(current_timestamp, 'YYYY'))::numeric  - 1), (to_char(current_timestamp, 'YYYY'))::numeric)
            and (
                sv.scenario_class_code = 'CRA'
                or sv.scenario_category_code in ('CHEF_STA' /*, 'CHEF_SUPP'*/)
            )
            and (
                sc.cra_supplemental_received_date is not null -- Has supplemental data
                or py.local_supplemntl_received_date is not null
            )
            and not exists (
                select *
                from farms.farm_scenarios_vw sv3
                where sv3.program_year_id = sv.program_year_id
                and (
                    -- Skip if there is an existing USER scenario (ignore Comparison Scenarios).
                    (sv3.scenario_class_code = 'USER' and sv3.scenario_category_code not in ('CS','UNK'))
                    -- Skip if there is already a Benefit Triage scenario.
                    or (sv3.scenario_class_code = 'TRIAGE' and sv3.scenario_state_code in ('COMPLETED', 'FAILED'))
                )
            )
        )
        select scenarios.participant_pin,
               scenarios.program_year,
               scenarios.cra_pyv_id,
               scenarios.cra_scenario_id,
               scenarios.cra_scenario_number
        from scenarios
        where scenarios.agristability_scenario_id = scenarios.cra_scenario_id
        order by scenarios.program_year,
                 scenarios.participant_pin;

    return cur;
end;
$$;
