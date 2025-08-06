create or replace view farms.scenarios_vw as
select ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       sc.scenario_number,
       coalesce(p.corp_name, p.last_name||', '||p.first_name) client_name,
       p.corp_name,
       p.first_name,
       p.last_name,
       sc.scenario_class_code,
       sc.scenario_state_code,
       sc.scenario_category_code,
       pyv.municipality_code,
       mc.description municipality_code_description,
       sc.agristability_scenario_id,
       ac.agristability_client_id,
       py.program_year_id,
       pyv.program_year_version_id,
       sc.combined_farm_number,
       sub.chef_submission_guid
from farms.farm_agristability_clients ac
join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
join farms.farm_agristability_scenarios sc on sc.program_year_version_id = pyv.program_year_version_id
join farms.farm_persons p on p.person_id = ac.person_id
join farms.farm_scenario_category_codes scc on scc.scenario_category_code = sc.scenario_category_code
join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = sc.scenario_state_code
left outer join farms.farm_municipality_codes mc on mc.municipality_code = pyv.municipality_code
left outer join farms.farm_chef_submissions sub on sub.chef_submission_id = sc.chef_submission_id;
