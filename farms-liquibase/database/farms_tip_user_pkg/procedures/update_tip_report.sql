create or replace procedure farms_tip_user_pkg.update_tip_report(
   in in_tip_report_id farms.farm_tip_report_documents.tip_report_document_id%type,
   in in_user farms.farm_tip_report_documents.who_created%type
)
language plpgsql
as $$
begin
    update farms.farm_tip_report_documents
    set generation_date = current_timestamp,
        document = '',
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where tip_report_document_id = in_tip_report_id;
end;
$$;
