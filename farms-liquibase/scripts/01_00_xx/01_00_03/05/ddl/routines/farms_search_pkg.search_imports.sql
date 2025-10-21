create or replace function farms_search_pkg.search_imports(
    in in_search_by_class_code text,
    in in_import_class_codes varchar[]
)
returns refcursor
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
               iv.when_updated,
               iv.import_state_code,
               iv.import_class_code,
               isc.description import_state_description,
               icc.description import_class_description
        from farms.farm_import_versions iv
        join farms.farm_import_state_codes isc on isc.import_state_code = iv.import_state_code
        join farms.farm_import_class_codes icc on icc.import_class_code = iv.import_class_code
        where ((in_search_by_class_code = 'Y' and iv.import_class_code = any(in_import_class_codes)))
        or (in_search_by_class_code = 'N' and iv.import_class_code not in ('ENROL', 'XENROL', 'XCONTACT', 'XSTATE', 'TIP_REPORT', 'FIFO'))
        order by when_updated desc;
    return v_cursor;
end;
$$;
