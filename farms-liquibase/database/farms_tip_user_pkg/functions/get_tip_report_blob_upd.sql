create or replace function farms_tip_user_pkg.get_tip_report_blob_upd(
   in in_tip_report_id farms.farm_tip_report_documents.tip_report_document_id%type,
   in in_farming_operation_id farms.farm_tip_report_documents.farming_operation_id%type
)
returns refcursor
language plpgsql
as $$
declare

    v_cursor refcursor;

begin
    open v_cursor for
      select docs.document
      from farms.farm_tip_report_documents docs
      where (docs.tip_report_document_id = in_tip_report_id
             or docs.farming_operation_id = in_farming_operation_id)
      for update;
    return v_cursor;
end;
$$;
