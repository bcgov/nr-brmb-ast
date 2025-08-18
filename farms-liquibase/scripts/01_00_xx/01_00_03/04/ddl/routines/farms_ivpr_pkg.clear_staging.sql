create or replace procedure farms_ivpr_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.farm_zivpr_iv_premium_rates;
    truncate table farms.farm_import_logs;
end;
$$;
