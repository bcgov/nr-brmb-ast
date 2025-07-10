create or replace procedure farms_staging_pkg.insert_z99(
   in in_extract_file_number farms.z99_extract_file.extract_file_number%type,
   in in_extract_date farms.z99_extract_file.extract_date%type,
   in in_row_count farms.z99_extract_file.row_count%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z99_extract_file (
        extract_file_number,
        extract_date,
        row_count,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_extract_file_number,
        in_extract_date,
        in_row_count,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
