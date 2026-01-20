create or replace function farms_import_xml_pkg.get_import_top_level_errors(
    in in_import_version_id farms.farm_import_versions.import_version_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select temp_table.file_number,
               temp_table.line_number,
               temp_table.message
        from farms.farm_import_versions iv,
        xmltable(
           '//IMPORT_LOG/ERROR'
           passing xml(iv.import_audit_info)
           columns
               file_number varchar(20) path '@fileNumber',
               line_number varchar(20) path '@rowNumber',
               message varchar(2000) path 'text()'
        ) temp_table
        where iv.import_version_id = in_import_version_id
        order by file_number,
                 line_number::numeric;
    return v_cursor;
end;
$$;
