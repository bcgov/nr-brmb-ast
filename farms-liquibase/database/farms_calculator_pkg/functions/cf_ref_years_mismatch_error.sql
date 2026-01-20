create or replace function farms_calculator_pkg.cf_ref_years_mismatch_error(
    in in_pin_to_add farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_cur_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns bigint
language plpgsql
as
$$
declare

    v_scenario_id_to_add farms.farm_agristability_scenarios.agristability_scenario_id%type;
    v_diff_count_1 bigint;
    v_diff_count_2 bigint;
    v_result bigint := 0;

begin
    v_scenario_id_to_add := farms_calculator_pkg.cf_get_sc_id_to_add(in_pin_to_add, in_program_year);

    select count(*)
    into v_diff_count_1
    from (
        select py.year
        from farms.farm_reference_scenarios rs
        join farms.farm_agristability_scenarios rsc on rsc.agristability_scenario_id = rs.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = rsc.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        where rs.for_agristability_scenario_id = in_cur_scenario_id
        except
        select py.year
        from farms.farm_reference_scenarios rs
        join farms.farm_agristability_scenarios rsc on rsc.agristability_scenario_id = rs.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = rsc.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        where rs.for_agristability_scenario_id = v_scenario_id_to_add
    ) alias1;

    select count(*)
    into v_diff_count_2
    from (
        select py.year
        from farms.farm_reference_scenarios rs
        join farms.farm_agristability_scenarios rsc on rsc.agristability_scenario_id = rs.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = rsc.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        where rs.for_agristability_scenario_id = v_scenario_id_to_add
        except
        select py.year
        from farms.farm_reference_scenarios rs
        join farms.farm_agristability_scenarios rsc on rsc.agristability_scenario_id = rs.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = rsc.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        where rs.for_agristability_scenario_id = in_cur_scenario_id
    ) alias1;

    if v_diff_count_1 > 0 or v_diff_count_2 > 0 then
        v_result := 1;
    end if;

    return v_result;
end;
$$;
