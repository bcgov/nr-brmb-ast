create or replace procedure farms_fmv_pkg.insert_staging_row(
   in in_line_number farms.zfmv_fair_market_value.line_number%type,
   in in_program_year farms.zfmv_fair_market_value.program_year%type,
   in in_period farms.zfmv_fair_market_value.period%type,
   in in_average_price farms.zfmv_fair_market_value.average_price%type,
   in in_percent_variance farms.zfmv_fair_market_value.percent_variance%type,
   in in_municipality_code farms.zfmv_fair_market_value.municipality_code%type,
   in in_crop_unit_code farms.zfmv_fair_market_value.crop_unit_code%type,
   in in_inventory_item_code farms.zfmv_fair_market_value.inventory_item_code%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.zfmv_fair_market_value (
        line_number,
        program_year,
        period,
        average_price,
        percent_variance,
        municipality_code,
        crop_unit_code,
        inventory_item_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_line_number,
        in_program_year,
        in_period,
        in_average_price,
        in_percent_variance,
        in_municipality_code,
        in_crop_unit_code,
        in_inventory_item_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
