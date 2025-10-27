create or replace function farms_calculator_pkg.cf_has_accounting_code_error(
    in in_pin_to_add farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_cur_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns bigint
language plpgsql
as
$$
declare

    v_scenario_id_to_add farms.farm_agristability_scenarios.agristability_scenario_id%type;
    v_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type;
    v_accounting_code_count bigint;
    v_result bigint := 0;

begin
    v_scenario_id_to_add := farms_calculator_pkg.cf_get_sc_id_to_add(in_pin_to_add, in_program_year);

    select sc.combined_farm_number
    into v_combined_farm_number
    from farms.farm_agristability_scenarios sc
    where sc.agristability_scenario_id = in_cur_scenario_id;

    select count(distinct fo.federal_accounting_code)
    into v_accounting_code_count
    from farms.farm_agristability_scenarios sc
    join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
    where sc.combined_farm_number = v_combined_farm_number
    or sc.agristability_scenario_id = v_scenario_id_to_add
    or sc.agristability_scenario_id = in_cur_scenario_id;

    if v_accounting_code_count > 1 then
        v_result := 1;
    end if;

    return v_result;
end;
$$;
