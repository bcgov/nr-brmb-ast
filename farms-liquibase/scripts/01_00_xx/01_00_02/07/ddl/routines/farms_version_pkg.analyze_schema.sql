create or replace procedure farms_version_pkg.analyze_schema(
   in in_version_id numeric
)
language plpgsql
as $$
begin
    call farms_import_pkg.update_status(in_version_id, 'Analyzing schema');
    analyze farms;
end;
$$;
