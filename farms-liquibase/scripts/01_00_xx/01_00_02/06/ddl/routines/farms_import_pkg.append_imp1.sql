create or replace procedure farms_import_pkg.append_imp1(
   in in_version_id numeric,
   in in_msg varchar
)
language plpgsql
as $$
declare
    eatit numeric;
begin
    eatit := farms_import_pkg.append_imp(in_version_id, in_msg);
end;
$$;
