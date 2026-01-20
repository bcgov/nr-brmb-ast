create or replace procedure farms_staging_pkg.insert_z99(
   in in_extract_file_number farms.farm_z99_extract_file_ctls.extract_file_number%type,
   in in_extract_date farms.farm_z99_extract_file_ctls.extract_date%type,
   in in_row_count farms.farm_z99_extract_file_ctls.row_count%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z99_extract_file_ctls (
        extract_file_number,
        extract_date,
        row_count,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
