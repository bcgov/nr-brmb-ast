create or replace view farms.operations_vw as
select ac.agristability_client_id,
       py.program_year_id,
       pyv.program_year_version_id,
       op.farming_operation_id,
       ac.participant_pin,
       py.year,
       pyv.program_year_version_number,
       op.operation_number,
       pyv.municipality_code
from farms.agristability_client ac,
     farms.program_year py,
     farms.program_year_version pyv,
     farms.farming_operation op
where py.agristability_client_id = ac.agristability_client_id
and pyv.program_year_id = py.program_year_id
and op.program_year_version_id = pyv.program_year_version_id;
