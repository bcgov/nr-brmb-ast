create or replace function farms_tip_user_pkg.get_tip_report_document_id(
   in in_farming_operation_id farms.farm_tip_report_documents.farming_operation_id%type
)
returns farms.farm_tip_report_documents.tip_report_document_id%type
language plpgsql
as $$
declare

    v_tip_report_document_id farms.farm_tip_report_documents.tip_report_document_id%type;

begin

    select trd.tip_report_document_id
    into v_tip_report_document_id
    from farms.farm_tip_report_documents trd
    where trd.farming_operation_id = in_farming_operation_id;

    return v_tip_report_document_id;
end;
$$;
