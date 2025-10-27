create or replace procedure farms_calculator_pkg.update_non_participant_ind(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_revision_count farms.farm_agristability_scenarios.revision_count%type,
    in in_non_participant_ind farms.farm_program_years.non_participant_ind%type,
    in in_user farms.farm_program_years.assigned_to_userid%type
)
language plpgsql
as
$$
declare

    v_program_year_id farms.farm_program_years.program_year_id%type;

begin

    select pyv.program_year_id
    into v_program_year_id
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    where s.agristability_scenario_id = in_scenario_id;

    update farms.farm_program_years
    set non_participant_ind = in_non_participant_ind,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where program_year_id = v_program_year_id;

    call farms_calculator_pkg.update_all_sc_rev(in_scenario_id, in_scenario_revision_count, in_user);

end;
$$;
