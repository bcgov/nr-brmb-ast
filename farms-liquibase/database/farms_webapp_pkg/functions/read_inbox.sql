create or replace function farms_webapp_pkg.read_inbox(
    in in_search_type text,
    in in_year farms.farm_program_years.year%type,
    in in_user_guid farms.farm_program_years.assigned_to_user_guid%type,
    in in_scenario_state_codes varchar[]
) returns refcursor
language plpgsql
as
$$
declare

    return_cur refcursor;

begin

    open return_cur for
        select m.agristability_scenario_id,
               coalesce(o.corp_name, o.last_name || case when (o.last_name is not null and o.last_name::text <> '') and (o.first_name is not null and o.first_name::text <> '') then ', ' end || o.first_name) client_name,
               m.participant_pin,
               s.scenario_state_code,
               ssc.description scenario_state_code_desc,
               s.when_updated last_changed,
               cl.total_benefit,
               py.assigned_to_userid,
               py.assigned_to_user_guid,
               pyv.received_date recieved_date
        from (
            select sv.agristability_scenario_id,
                   sv.participant_pin,
                   first_value(sv.agristability_scenario_id) over (partition by sv.program_year_id order by case when s2.scenario_class_code != 'REF' then 0 else 1 end, sv.scenario_number desc) sc_m_id_ready,
                   first_value(sv.agristability_scenario_id) over (partition by sv.program_year_id order by case when s2.scenario_class_code != 'REF' then 0 else 1 end, case when(s2.default_ind = 'y') then 0 else 1 end, case when s2.scenario_class_code = 'USER' then 0 else 1 end, sv.scenario_number desc) sc_m_id_user,
                   sv.program_year_id,
                   sv.agristability_client_id,
                   sv.program_year_version_id
            from farms.farm_scenarios_vw sv
            join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = sv.agristability_scenario_id
            where sv.year = in_year
            and s2.scenario_category_code != 'ENW'
        ) m
        join farms.farm_agristability_scenarios s on s.agristability_scenario_id = m.agristability_scenario_id
        join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = s.scenario_state_code
        join farms.farm_agristability_clients ac on ac.agristability_client_id = m.agristability_client_id
        join farms.farm_persons o on o.person_id = ac.person_id
        join farms.farm_program_years py on py.program_year_id = m.program_year_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = m.program_year_version_id
        left outer join farms.farm_agristability_claims cl on cl.agristability_scenario_id = m.agristability_scenario_id
        where (
            (in_search_type = 'READY' and m.sc_m_id_ready = m.agristability_scenario_id)
             or (in_search_type in ('USER', 'ALL_USERS') and m.sc_m_id_user = m.agristability_scenario_id)
        )
        and s.scenario_state_code = any(in_scenario_state_codes)
        and (in_search_type != 'USER' or py.assigned_to_user_guid = in_user_guid)
        and (in_search_type != 'READY' or s.scenario_class_code = 'GEN' or (exists (
            select *
            from farms.farm_farming_operations fo
            join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
            where fo.program_year_version_id = pyv.program_year_version_id)
            and not exists (
                select *
                from farms.farm_agristability_scenarios s
                where s.program_year_version_id = pyv.program_year_version_id
                and s.scenario_class_code = 'USER'
                and s.scenario_category_code != 'ENW')
            )
        );

    return return_cur;

end;
$$;
