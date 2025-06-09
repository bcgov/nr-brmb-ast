create or replace view farms.agri_scenarios_vw as
select ac.agristability_client_id,
       py.program_year_id,
       pyv.program_year_version_id,
       sc.agristability_scenario_id,
       ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       sc.scenario_number,
       sc.default_indicator
from farms.agristability_client ac,
     farms.program_year py,
     farms.program_year_version pyv,
     farms.agristability_scenario sc
where py.agristability_client_id = ac.agristability_client_id
and pyv.program_year_id = py.program_year_id
and sc.program_year_version_id = pyv.program_year_version_id;
