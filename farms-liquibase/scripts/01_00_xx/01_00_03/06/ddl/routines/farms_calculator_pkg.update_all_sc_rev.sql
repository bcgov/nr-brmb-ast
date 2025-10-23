create or replace procedure farms_calculator_pkg.update_all_sc_rev(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_revision_count farms.farm_agristability_scenarios.revision_count%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
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

    update farms.farm_agristability_scenarios s
    set s.revision_count = s.revision_count + 1,
        s.who_updated = in_user,
        s.when_updated = current_timestamp
    where s.agristability_scenario_id != in_scenario_id
    and s.scenario_class_code = 'USER'
    and s.program_year_version_id in (
        select pyv.program_year_version_id
        from farms.farm_program_year_versions pyv
        where pyv.program_year_id = v_program_year_id
    );

    call farms_calculator_pkg.update_sc_rev(in_scenario_id, in_revision_count, in_user);

end;
$$;
