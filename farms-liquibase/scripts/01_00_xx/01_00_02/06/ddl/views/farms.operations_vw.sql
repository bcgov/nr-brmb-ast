create or replace view farms.farm_operations_vw as
select ac.agristability_client_id,
       py.program_year_id,
       pyv.program_year_version_id,
       op.farming_operation_id,
       ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       op.operation_number,
       pyv.municipality_code
from farms.farm_agristability_clients ac,
     farms.farm_program_years py,
     farms.farm_program_year_versions pyv,
     farms.farm_farming_operations op
where py.agristability_client_id = ac.agristability_client_id
and pyv.program_year_id = py.program_year_id
and op.program_year_version_id = pyv.program_year_version_id;
