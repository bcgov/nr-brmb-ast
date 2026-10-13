create or replace function farms_calculator_pkg.cf_matches_verified(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_verified_cf_number farms.farm_agristability_scenarios.combined_farm_number%type
) returns bigint
language plpgsql
as
$$
declare

    v_missing_from_new bigint;
    v_missing_from_old bigint;
    v_result bigint := 0;

begin

    select count(*)
    into v_missing_from_new
    from (
        select ac.participant_pin
        from farms.farm_agristability_scenarios s
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        where s.combined_farm_number = in_verified_cf_number
        except
        select ac.participant_pin
        from farms.farm_agristability_scenarios s
        join farms.farm_agristability_scenarios s2 on s2.combined_farm_number = s.combined_farm_number
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s2.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        where s.agristability_scenario_id = in_agristability_scenario_id
    ) alias1;

    select count(*)
    into v_missing_from_old
    from (
        select ac.participant_pin
        from farms.farm_agristability_scenarios s
        join farms.farm_agristability_scenarios s2 on s2.combined_farm_number = s.combined_farm_number
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s2.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        where s.agristability_scenario_id = in_agristability_scenario_id
        except
        select ac.participant_pin
        from farms.farm_agristability_scenarios s
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        where s.combined_farm_number = in_verified_cf_number
    ) alias1;

    if v_missing_from_old = 0 and v_missing_from_new = 0 then
        v_result := 1;
    end if;

    return v_result;
end;
$$;
