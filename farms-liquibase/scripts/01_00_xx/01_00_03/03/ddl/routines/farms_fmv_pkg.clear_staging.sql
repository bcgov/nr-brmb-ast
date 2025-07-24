create or replace procedure farms_fmv_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.zfmv_fair_market_value;
    truncate table farms.import_log;
end;
$$;
