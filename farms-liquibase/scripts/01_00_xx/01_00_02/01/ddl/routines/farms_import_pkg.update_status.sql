create or replace procedure farms_import_pkg.update_status(
   in in_version_id numeric,
   in in_msg varchar
)
language plpgsql
as $$
begin
    call farms_import_pkg.update_status_non_autonomous(in_version_id, in_msg);
end;
$$;
