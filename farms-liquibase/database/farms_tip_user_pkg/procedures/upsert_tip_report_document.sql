create or replace procedure farms_tip_user_pkg.upsert_tip_report_document(
   inout io_tip_report_document_id farms.farm_tip_report_documents.tip_report_document_id%type,
   in in_farming_operation_id farms.farm_tip_report_documents.farming_operation_id%type,
   in in_user farms.farm_tip_report_documents.who_created%type
)
language plpgsql
as $$
begin

    update farms.farm_agristability_clients ac
    set tip_participant_ind = 'Y',
        revision_count = ac.revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where ac.agristability_client_id in (
      select py.agristability_client_id
      from farms.farm_farming_operations fo
      join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
      join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
      where fo.farming_operation_id = in_farming_operation_id
    );

    merge into farms.farm_tip_report_documents o
    using(
      select py.program_year_id,
             fo.alignment_key,
             fo.farming_operation_id
      from farms.farm_farming_operations fo
      join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
      join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
      where fo.farming_operation_id = in_farming_operation_id
    ) n
    on (o.program_year_id = n.program_year_id and o.alignment_key = n.alignment_key)
    when matched then
      update set
       generation_date = current_timestamp,
       document = '',
       farming_operation_id = n.farming_operation_id,
       revision_count = o.revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp
    when not matched then
      insert(
        tip_report_document_id,
        alignment_key,
        generation_date,
        document,
        program_year_id,
        farming_operation_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
      ) values (
        nextval('farms.farm_trd_seq'),
        n.alignment_key,
        current_timestamp,
        '',
        n.program_year_id,
        n.farming_operation_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
      );

    select trd.tip_report_document_id
    into io_tip_report_document_id
    from farms.farm_farming_operations fo
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    join farms.farm_tip_report_documents trd on trd.program_year_id = py.program_year_id
                                      and trd.alignment_key = fo.alignment_key
    where fo.farming_operation_id = in_farming_operation_id;

end;
$$;
