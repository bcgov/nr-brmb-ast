create or replace procedure farms_webapp_pkg.import_in_progress_check()
language plpgsql
as
$$
declare

    c_import_version cursor(p_state text) for
        select iv.import_version_id,
               iv.staging_audit_info,
               iv.import_audit_info
        from farms.farm_import_versions iv
        where iv.import_state_code = p_state;

    v_amount integer;
    v_offset integer := 1;
    v_err_msg varchar(500);

begin
    for v_staging in c_import_version('SP')
    loop
        update farms.farm_import_versions
        set import_state_code = 'SF'
        where import_version_id = v_staging.import_version_id;

        --
        -- For staging, the 210 screen only shows errors and warnings, so this
        -- can easily be shown as an error. Note that we want to overwrite
        -- the XML clob because we don't know if the current XML is well-formed or not.
        --
        v_err_msg := '<STAGING_LOG><ERRORS><ERROR>Application startup found this item in the Staging in Progress state.</ERROR></ERRORS></STAGING_LOG>';

        update farms.farm_import_versions
        set staging_audit_info = v_err_msg
        where import_version_id = v_staging.import_version_id;
    end loop;

    for v_importing in c_import_version('IP')
    loop
        update farms.farm_import_versions
        set import_state_code = 'IF'
        where import_version_id = v_importing.import_version_id;

        --
        -- For import, tthere can be several top-level errors
        --
        v_err_msg := '<IMPORT_LOG><ERROR>Application startup found this item in the Import in Progress state.</ERROR></IMPORT_LOG>';
        update farms.farm_import_versions
        set import_audit_info = v_err_msg
        where import_version_id = v_importing.import_version_id;
    end loop;
end;
$$;
