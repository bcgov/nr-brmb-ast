create or replace procedure farms_tip_user_pkg.update_tip_report_document(
   in in_tip_report_document_id farms.farm_tip_report_documents.tip_report_document_id%type,
   in in_document farms.farm_tip_report_documents.document%type,
   in in_user farms.farm_tip_report_documents.who_created%type
)
language plpgsql
as $$
begin
    update farms.farm_tip_report_documents
    set document = in_document,
        who_updated = in_user,
        when_updated = current_timestamp
    where tip_report_document_id = in_tip_report_document_id;
end;
$$;
