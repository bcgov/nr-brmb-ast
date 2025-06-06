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
from farms.agristability_client ac
join farms.program_year py on py.agristability_client_id = ac.agristability_client_id
join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
join farms.agristability_scenario sc on sc.program_year_version_id = pyv.program_year_version_id
join farms.person p on p.person_id = ac.person_id
join farms.scenario_category_code scc on scc.scenario_category_code = sc.scenario_category_code
join farms.scenario_state_code ssc on ssc.scenario_state_code = sc.scenario_state_code
left outer join farms.municipality_code mc on mc.municipality_code = pyv.municipality_code
left outer join farms.chef_submission sub on sub.chef_submission_id = sc.chef_submission_id;
