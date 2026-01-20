create or replace function farms_read_pkg.read_combined_farm_clients(
    in in_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select sc.agristability_scenario_id,
               sc.scenario_number,
               sc.revision_count scenario_revision_count,
               m.participant_pin,
               o.first_name,
               o.last_name,
               o.corp_name,
               cl.total_benefit
        from farms.farm_agristability_scenarios sc
        join farms.farm_agri_scenarios_vw m on m.agristability_scenario_id = sc.agristability_scenario_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = m.agristability_client_id
        join farms.farm_persons o on o.person_id = ac.person_id
        left outer join farms.farm_agristability_claims cl on cl.agristability_scenario_id = sc.agristability_scenario_id
        where sc.combined_farm_number = in_combined_farm_number
        order by m.participant_pin;
    return cur;
end;
$$;
