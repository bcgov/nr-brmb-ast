-- Kept to the four tables that identify a scenario so that it stays within the planner's
-- from_collapse_limit (8) and can be merged into the calling query. Use farm_scenario_broad_vw
-- for client names, municipality description or chef_submission_guid.
create or replace view farms.farm_scenarios_vw as
select ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       sc.scenario_number,
       sc.scenario_class_code,
       sc.scenario_state_code,
       sc.scenario_category_code,
       pyv.municipality_code,
       sc.agristability_scenario_id,
       ac.agristability_client_id,
       ac.person_id,
       ac.person_id_client_contacted_by,
       py.program_year_id,
       pyv.program_year_version_id,
       sc.combined_farm_number,
       sc.chef_submission_id
from farms.farm_agristability_clients ac
join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
join farms.farm_agristability_scenarios sc on sc.program_year_version_id = pyv.program_year_version_id;
