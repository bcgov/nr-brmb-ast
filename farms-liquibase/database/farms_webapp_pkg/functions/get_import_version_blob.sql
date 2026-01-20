create or replace function farms_webapp_pkg.get_import_version_blob(
    in in_id farms.farm_import_versions.import_version_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select iv.import_file
        from farms.farm_import_versions iv
        where import_version_id = in_id;
    return v_cursor;
end;
$$;
