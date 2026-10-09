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
        select ip_ac.participant_pin,
               ip.scenario_number
        from farms.farm_agristability_scenarios sc
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sc.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        join farms.farm_agristability_scenarios sc2 on sc2.combined_farm_number = sc.combined_farm_number
        join farms.farm_program_year_versions pyv2 on pyv2.program_year_version_id = sc2.program_year_version_id
        join farms.farm_program_year_versions ip_pyv on ip_pyv.program_year_id = pyv2.program_year_id
        join farms.farm_agristability_scenarios ip on ip.program_year_version_id = ip_pyv.program_year_version_id
        join farms.farm_program_years ip_py on ip_py.program_year_id = ip_pyv.program_year_id
        join farms.farm_agristability_clients ip_ac on ip_ac.agristability_client_id = ip_py.agristability_client_id
        where ip.scenario_class_code = 'USER'
        and ip.scenario_state_code = 'IP'
        and ip.scenario_category_code != 'UNK'
        and ip_pyv.municipality_code = pyv.municipality_code
        and sc.combined_farm_number = in_combined_farm_number
        and ac.participant_pin = in_participant_pin
        order by ip_ac.participant_pin, ip.scenario_number;
    return cur;
end;
$$;
