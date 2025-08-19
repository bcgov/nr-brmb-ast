create or replace procedure farms_ivpr_pkg.insert_error(
   in in_import_version_id numeric,
   in in_line_number numeric,
   in in_msg varchar
)
language plpgsql
as $$
declare
    v_xml_msg varchar(2000);
begin
    v_xml_msg := '<ERROR rowNumber="' || in_line_number || '">' || in_msg || '</ERROR>';

    insert into farms.farm_import_logs (
        import_log_id,
        log_message,
        import_version_id
    ) values (
        nextval('farms.farm_ilg_seq'),
        v_xml_msg,
        in_import_version_id
    );
end;
$$;
