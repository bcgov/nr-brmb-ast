create or replace procedure farms_import_pkg.update_imp(
   in in_id numeric,
   in in_msg varchar
)
language plpgsql
as $$
begin
    update farms.import_log
    set log_message = in_msg
    where import_log_id = in_id;
end;
$$;
