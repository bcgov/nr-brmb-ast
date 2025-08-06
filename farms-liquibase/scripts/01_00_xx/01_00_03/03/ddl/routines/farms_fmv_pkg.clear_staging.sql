create or replace procedure farms_fmv_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.farm_zfmv_fair_market_values;
    truncate table farms.farm_import_logs;
end;
$$;
