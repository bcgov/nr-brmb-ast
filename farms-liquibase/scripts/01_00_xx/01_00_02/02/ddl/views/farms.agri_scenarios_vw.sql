create or replace view farms.farm_agri_scenarios_vw as
select ac.agristability_client_id,
       py.program_year_id,
       pyv.program_year_version_id,
       sc.agristability_scenario_id,
       ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       sc.scenario_number,
       sc.default_ind
from farms.farm_agristability_clients ac,
     farms.farm_program_years py,
     farms.farm_program_year_versions pyv,
     farms.farm_agristability_scenarios sc
where py.agristability_client_id = ac.agristability_client_id
and pyv.program_year_id = py.program_year_id
and sc.program_year_version_id = pyv.program_year_version_id;
