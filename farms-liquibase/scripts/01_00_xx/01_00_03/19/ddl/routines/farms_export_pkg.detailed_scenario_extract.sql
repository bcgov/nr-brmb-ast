create or replace function farms_export_pkg.detailed_scenario_extract(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select ac.participant_pin pin,
               case
                   when coalesce(per.corp_name::text, '') = '' then per.first_name || ' ' || per.last_name
                   else per.corp_name
               end client_name,
               (
                   case
                       when py.assigned_to_userid like 'IDIR\%' then substr(py.assigned_to_userid, 6)
                       else py.assigned_to_userid
                   end
               ) scn_checked_out,
               (
                   case
                       when scn.scenario_created_by like 'IDIR\%' then substr(scn.scenario_created_by, 6)
                       else scn.scenario_created_by
                   end
               ) scn_created_by,
               py.year program_year,
               scn.cra_income_expns_received_date inc_exp_received_date,
               scn.cra_supplemental_received_date,
               py.local_supplemntl_received_date,
               scn.scenario_number scn_number,
               scn.default_ind "DEFAULT",
               ssc.description scn_state,
               scc.description scn_type,
               scc1.description scn_category,
               scn.description scn_description
        from farms.farm_program_years py
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_agristability_scenarios scn on scn.program_year_version_id = pyv.program_year_version_id
        join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = scn.scenario_state_code
        join farms.farm_scenario_class_codes scc on scc.scenario_class_code = scn.scenario_class_code
        join farms.farm_scenario_category_codes scc1 on scc1.scenario_category_code = scn.scenario_category_code
        join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
        join farms.farm_persons per on per.person_id = ac.person_id
        where py.year = in_program_year
        and scn.scenario_class_code in ('CRA', 'USER', 'GEN', 'LOCAL')
        order by pin, scn_number;

    return cur;
end;
$$;
