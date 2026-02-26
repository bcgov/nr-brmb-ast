create or replace procedure farms_aarm_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.farm_zaarm_margins;
    truncate table farms.farm_import_logs;
end;
$$;
