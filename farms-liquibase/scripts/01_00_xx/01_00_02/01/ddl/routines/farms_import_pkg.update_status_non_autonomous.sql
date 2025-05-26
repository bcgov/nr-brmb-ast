create or replace procedure farms_import_pkg.update_status_non_autonomous(
   in in_version_id numeric,
   in in_msg varchar
)
language plpgsql
as $$
begin
    update farms.import_version
    set last_status_message = in_msg,
        last_status_date = current_date
    where import_version_id = in_version_id;
end;
$$;
