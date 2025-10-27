create or replace procedure farms_calculator_pkg.update_pin_rev_counts(
    in in_pins varchar[],
    in in_program_year farms.farm_program_years.year%type,
    in in_flag_reasonabilitytests_stale text,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id in (
        select sv.agristability_scenario_id
        from farms.farm_scenarios_vw sv
        where sv.participant_pin = any(in_pins)
        and sv.year = in_program_year
        and sv.scenario_class_code = 'USER'
        and sv.scenario_state_code = 'IP'
    );

    if in_flag_reasonabilitytests_stale = 'Y' then

        update farms.farm_reasonabilty_test_results
        set fresh_ind = 'N',
            revision_count = revision_count + 1,
            who_updated = in_user,
            when_updated = current_timestamp
        where agristability_scenario_id in (
            select sv.agristability_scenario_id
            from farms.farm_scenarios_vw sv
            where sv.participant_pin = any(in_pins)
            and sv.year = in_program_year
            and sv.scenario_class_code = 'USER'
            and sv.scenario_state_code = 'IP'
        );

    end if;

end;
$$;
