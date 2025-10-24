create or replace procedure farms_calculator_pkg.assign_to_user(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_revision_count farms.farm_agristability_scenarios.revision_count%type,
    in in_user_guid farms.farm_program_years.assigned_to_user_guid%type,
    in in_user farms.farm_program_years.assigned_to_userid%type
)
language plpgsql
as
$$
declare

    v_program_year_id farms.farm_program_years.program_year_id%type;

begin

    select sv.program_year_id
    into v_program_year_id
    from farms.farm_scenarios_vw sv
    where sv.agristability_scenario_id = in_scenario_id;

    update farms.farm_program_years py
    set py.assigned_to_userid = in_user,
        py.assigned_to_user_guid = in_user_guid,
        py.who_updated = in_user,
        py.when_updated = current_timestamp
    where py.program_year_id = v_program_year_id;

    call farms_calculator_pkg.update_all_sc_rev(in_scenario_id, in_scenario_revision_count, in_user);

end;
$$;
