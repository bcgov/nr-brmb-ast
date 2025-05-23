create or replace procedure farms_version_pkg.update_control_file_info_stg(
   in in_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    p_import_control_file_date farms.import_version.import_control_file_date%type;
    p_import_control_file_info farms.import_version.import_control_file_information%type;
begin
    begin
        select max(to_date(t.extract_date, 'yyyymmdd'))
        into p_import_control_file_date
        from farms.z99_extract_file;
    exception
        when others then
            null;
    end;

    begin
        select 'Imported ' || t.row_cnt || ' representing ' || t.pins ||
            ' Participant Years.'
        into p_import_control_file_info
        from (
            select max(case when extract_file_number = 1 then row_count else null end) as pins,
                   sum(row_count) as row_cnt
            from farms.z99_extract_file
        ) as t;
    exception
        when others then
            null;
    end;

    update farms.import_version
    set import_control_file_date = p_import_control_file_date,
        import_control_file_information = p_import_control_file_info,
        revision_count = revision_count + 1,
        update_user = in_user,
        update_date = current_timestamp
    where import_version_id = in_version_id;
end;
$$;
