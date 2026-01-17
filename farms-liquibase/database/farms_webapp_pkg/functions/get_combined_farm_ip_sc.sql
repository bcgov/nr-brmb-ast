create or replace function farms_webapp_pkg.get_combined_farm_ip_sc(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select ipsv.participant_pin,
               ip.scenario_number
        from farms.farm_agristability_scenarios sc
        join farms.farm_scenarios_vw sv on sv.agristability_scenario_id = sc.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sc.program_year_version_id
        join farms.farm_agristability_scenarios sc2 on sc2.combined_farm_number = sc.combined_farm_number
        join farms.farm_scenarios_vw sv2 on sv2.agristability_scenario_id = sc2.agristability_scenario_id
        join farms.farm_scenarios_vw ipsv on ipsv.program_year_id = sv2.program_year_id
        join farms.farm_agristability_scenarios ip on ip.agristability_scenario_id = ipsv.agristability_scenario_id
        join farms.farm_program_year_versions ip_pyv on ip_pyv.program_year_version_id = ip.program_year_version_id
        where ip.scenario_class_code = 'USER'
        and ip.scenario_state_code = 'IP'
        and ip.scenario_category_code != 'UNK'
        and ip_pyv.municipality_code = pyv.municipality_code
        and sc.combined_farm_number = in_combined_farm_number
        and sv.participant_pin = in_participant_pin
        order by ipsv.participant_pin, ip.scenario_number;
    return cur;
end;
$$;
