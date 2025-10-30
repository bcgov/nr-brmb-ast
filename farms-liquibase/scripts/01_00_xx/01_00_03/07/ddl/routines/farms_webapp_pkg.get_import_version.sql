create or replace function farms_webapp_pkg.get_import_version(
    in in_id farms.farm_import_versions.import_version_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select iv.import_version_id,
               iv.imported_by_user,
               iv.description,
               iv.import_file_name,
               farms_webapp_pkg.decrypt(iv.import_file_password) import_file_password,
               iv.import_control_file_date,
               iv.import_control_file_info,
               iv.import_date,
               iv.import_state_code,
               iv.import_class_code,
               isc.description import_state_description,
               icc.description import_class_description,
               iv.last_status_message,
               iv.last_status_date,
               case when exists (
                   select null
                   from farms.farm_import_versions iv2
                   where iv2.import_class_code = iv.import_class_code
                   and iv2.when_updated > iv.when_updated
               ) then 'N' else 'Y' end latest_of_class_ind,
               iv.import_audit_info
        from farms.farm_import_versions iv
        join farms.farm_import_state_codes isc on isc.import_state_code = iv.import_state_code
        join farms.farm_import_class_codes icc on icc.import_class_code = iv.import_class_code
        where import_version_id = in_id;
    return v_cursor;
end;
$$;
