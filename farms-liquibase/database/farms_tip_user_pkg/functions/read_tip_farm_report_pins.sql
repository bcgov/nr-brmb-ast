create or replace function farms_tip_user_pkg.read_tip_farm_report_pins(
   in in_year farms.farm_program_years.year%type
)
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin

    open cur for

      with op as (
        select pyv.program_year_version_id,
               fo.farming_operation_id,
               first_value(pyv.program_year_version_id) over (partition by py.program_year_id order by pyv.program_year_version_number desc) client_latest_pyv_id
        from farms.farm_program_years py
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
        join farms.farm_agristability_scenarios sc on sc.program_year_version_id = pyv.program_year_version_id
        where sc.scenario_class_code = 'CRA'
          and fo.locally_generated_ind = 'N'
          and py.year = in_year
      )
      select ac.participant_pin,
             coalesce(o.corp_name, o.last_name || ', ' || o.first_name) producer_name,
             fo.farming_operation_id,
             fo.partnership_pin,
             trd.tip_report_document_id,
             case when (trd.tip_report_document_id is not null and trd.tip_report_document_id::text <> '') then 'GENERATED'
                  when (trr.farm_type_level is not null and trr.farm_type_level::text <> '') then 'UNGENERATED'
                  when (trr.farm_type_1_name is not null and trr.farm_type_1_name::text <> '') then 'UNGROUPED'
                  else 'UNCLASSIFIED'
             end tip_report_status,
             ac.tip_participant_ind,
               case when exists (select *
                                 from farms.farm_program_years py2
                                 join farms.farm_program_year_versions pyv2 on pyv2.program_year_id = py2.program_year_id
                                 join farms.farm_agristability_scenarios sc2 on sc2.program_year_version_id = pyv2.program_year_version_id
                                 where py2.agristability_client_id = ac.agristability_client_id
                                   and sc2.scenario_class_code = 'USER'
                                   and sc2.scenario_state_code in ('IP', 'COMP')
                                   and sc2.scenario_category_code in ('FIN') 
                                )
               then 'Y' else 'N' end
             agristability_participant_ind
      from op
      join farms.farm_farming_operations fo on op.farming_operation_id = fo.farming_operation_id
      join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
      join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
      join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
      join farms.farm_persons o on o.person_id = ac.person_id
      left outer join farms.farm_tip_report_results trr on trr.program_year_id = py.program_year_id
                                                 and trr.alignment_key = fo.alignment_key
                                                 and coalesce(trr.parent_tip_report_result_id::text, '') = ''
      left outer join farms.farm_tip_report_documents trd on trd.program_year_id = py.program_year_id
                                                   and trd.alignment_key = fo.alignment_key 
      where op.program_year_version_id = op.client_latest_pyv_id;

    return cur;
end;
$$;
