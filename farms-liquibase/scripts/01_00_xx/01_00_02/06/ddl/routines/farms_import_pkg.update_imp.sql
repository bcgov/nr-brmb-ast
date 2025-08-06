create or replace procedure farms_import_pkg.update_imp(
   in in_id numeric,
   in in_msg varchar
)
language plpgsql
as $$
begin
    update farms.farm_import_logs
    set log_message = in_msg
    where import_log_id = in_id;
end;
$$;
