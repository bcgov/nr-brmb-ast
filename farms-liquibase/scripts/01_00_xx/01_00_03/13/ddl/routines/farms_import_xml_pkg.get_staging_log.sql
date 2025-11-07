create or replace function farms_import_xml_pkg.get_staging_log(
    in_import_version_id farms.farm_import_versions.import_version_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select staging_audit_info
        from farms.farm_import_versions
        where import_version_id = in_import_version_id;
    return v_cursor;
end;
$$;
