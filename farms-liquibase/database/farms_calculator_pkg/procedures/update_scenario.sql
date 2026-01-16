create or replace procedure farms_calculator_pkg.update_scenario(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_description farms.farm_agristability_scenarios.description%type,
    in in_scenario_state_code farms.farm_agristability_scenarios.scenario_state_code%type,
    in in_state_change_reason farms.farm_scenario_state_audits.state_change_reason%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_default_ind farms.farm_agristability_scenarios.default_ind%type,
    in in_verifier_user_id farms.farm_agristability_scenarios.verifier_user_id%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
declare

    old_scenario_state_code farms.farm_agristability_scenarios.scenario_state_code%type;
    old_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type;
    new_default_ind farms.farm_agristability_scenarios.default_ind%type := in_default_ind;

    active_scenarios_curs cursor for
        select sc.agristability_scenario_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
        join farms.farm_program_year_versions pyv2 on pyv2.program_year_id = pyv.program_year_id
        join farms.farm_agristability_scenarios sc2 on pyv2.program_year_version_id = sc2.program_year_version_id
        where sc2.agristability_scenario_id = in_agristability_scenario_id
        and sc.scenario_class_code = 'USER'
        and sc.scenario_state_code = 'IP'
        and sc.scenario_category_code != 'NOL'
        and sc.agristability_scenario_id != in_agristability_scenario_id;

    verified_scenarios_curs cursor for
        select sc.agristability_scenario_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
        join farms.farm_program_year_versions pyv2 on pyv2.program_year_id = pyv.program_year_id
        join farms.farm_agristability_scenarios sc2 on pyv2.program_year_version_id = sc2.program_year_version_id
        where sc2.agristability_scenario_id = in_agristability_scenario_id
        and sc.scenario_class_code = 'USER'
        and sc.scenario_state_code = 'COMP';

begin

    begin
        select sc.scenario_state_code,
               sc.scenario_category_code
        into strict old_scenario_state_code,
                    old_scenario_category_code
        from farms.farm_agristability_scenarios sc
        where sc.agristability_scenario_id = in_agristability_scenario_id;
    exception
        when no_data_found then
            raise exception '%', farms_types_pkg.scenario_not_found_msg()
            using errcode = farms_types_pkg.scenario_not_found_num()::text;
    end;

    if in_scenario_state_code <> old_scenario_state_code then

        if in_scenario_state_code = 'COMP' then
            new_default_ind := 'Y';

            for verified_scenario in verified_scenarios_curs
            loop
                call farms_calculator_pkg.update_scenario_state(verified_scenario.agristability_scenario_id,
                                                                'AMEND',
                                                                'Scenario was set to Amended when another scenario was Verified.',
                                                                in_user);
            end loop;

            for active_scenario in active_scenarios_curs
            loop
                call farms_calculator_pkg.update_scenario_state(active_scenario.agristability_scenario_id,
                                                                'CLO',
                                                                'Scenario was Closed when another scenario was Verified.',
                                                                in_user);
            end loop;

            update farms.farm_agristability_scenarios
            set default_ind = 'Y'
            where agristability_scenario_id = in_agristability_scenario_id;
        end if;

        call farms_calculator_pkg.update_scenario_state(in_agristability_scenario_id,
                                                        in_scenario_state_code,
                                                        in_state_change_reason,
                                                        in_user);

    end if;

    if in_scenario_category_code <> old_scenario_category_code then
        call farms_calculator_pkg.update_scenario_category(in_agristability_scenario_id,
                                                           in_scenario_category_code,
                                                           in_user);
    end if;

    if new_default_ind = 'Y' then
        update farms.farm_agristability_scenarios
        set default_ind = 'N'
        where agristability_scenario_id in (
            select sc.agristability_scenario_id
            from farms.farm_agristability_scenarios sc
            join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
            join farms.farm_program_year_versions pyv2 on pyv2.program_year_id = pyv.program_year_id
            join farms.farm_agristability_scenarios sc2 on pyv2.program_year_version_id = sc2.program_year_version_id
            where sc2.agristability_scenario_id = in_agristability_scenario_id
            and sc.default_ind = 'Y'
        );
    end if;

    update farms.farm_agristability_scenarios
    set description = in_description,
        default_ind = new_default_ind,
        verifier_user_id = in_verifier_user_id,
        when_updated = current_timestamp,
        who_updated = in_user
    where agristability_scenario_id = in_agristability_scenario_id;

end;
$$;
