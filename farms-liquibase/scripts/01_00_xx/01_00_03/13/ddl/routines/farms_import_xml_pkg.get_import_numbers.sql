create or replace function farms_import_xml_pkg.get_import_numbers(
    in in_import_version_id farms.farm_import_versions.import_version_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select temp_table.num_total,
               temp_table.num_new,
               temp_table.num_updates,
               temp_table.num_value_updates
        from farms.farm_import_versions iv,
        xmltable(
            '//IMPORT_LOG'
            passing xml(iv.import_audit_info)
            columns
                num_total varchar(20) path 'NUM_NEW/text()',
                num_updates varchar(20) path 'NUM_UPDATED/text()',
                num_value_updates varchar(20) path 'NUM_VALUES_UPDATED/text()'
        ) temp_table
        where iv.import_version_id = in_import_version_id;
    return v_cursor;
end;
$$;
