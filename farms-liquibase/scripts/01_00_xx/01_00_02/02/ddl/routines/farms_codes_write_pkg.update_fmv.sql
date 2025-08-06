create or replace procedure farms_codes_write_pkg.update_fmv(
   in in_fair_market_value_id farms.farm_fair_market_values.fair_market_value_id%type,
   in in_program_year farms.farm_fair_market_values.program_year%type,
   in in_period farms.farm_fair_market_values.period%type,
   in in_average_price farms.farm_fair_market_values.average_price%type,
   in in_percent_variance farms.farm_fair_market_values.percent_variance%type,
   in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
   in in_municipality_code farms.farm_fair_market_values.municipality_code%type,
   in in_crop_unit_code farms.farm_fair_market_values.crop_unit_code%type,
   in in_revision_count farms.farm_fair_market_values.revision_count%type,
   in in_user farms.farm_fair_market_values.who_updated%type
)
language plpgsql
as $$
begin

    call farms_codes_write_pkg.expire_fmv(
        in_fair_market_value_id,
        in_program_year,
        in_period,
        in_inventory_item_code,
        in_municipality_code,
        in_revision_count,
        in_user
    );

    if in_average_price is not null and in_percent_variance is not null then
        call farms_codes_write_pkg.create_fmv(
            in_program_year,
            in_period,
            in_average_price,
            in_percent_variance,
            in_inventory_item_code,
            in_municipality_code,
            in_crop_unit_code,
            in_user
        );
    end if;

end;
$$;
