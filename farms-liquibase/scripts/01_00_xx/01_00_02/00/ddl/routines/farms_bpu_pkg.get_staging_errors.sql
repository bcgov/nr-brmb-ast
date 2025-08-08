create or replace function farms_bpu_pkg.get_staging_errors(
    in in_import_version_id numeric
)
returns refcursor
language plpgsql
as
$$
declare
    v_cursor refcursor;
begin
    open v_cursor for
        select log_message
        from farms.farm_import_logs
        where import_version_id = in_import_version_id
        order by import_log_id;

    return v_cursor;
end;
$$;
