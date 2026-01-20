create or replace procedure farms_fmv_pkg.insert_staging_row(
   in in_line_number farms.farm_zfmv_fair_market_values.line_number%type,
   in in_program_year farms.farm_zfmv_fair_market_values.program_year%type,
   in in_period farms.farm_zfmv_fair_market_values.period%type,
   in in_average_price farms.farm_zfmv_fair_market_values.average_price%type,
   in in_percent_variance farms.farm_zfmv_fair_market_values.percent_variance%type,
   in in_municipality_code farms.farm_zfmv_fair_market_values.municipality_code%type,
   in in_crop_unit_code farms.farm_zfmv_fair_market_values.crop_unit_code%type,
   in in_inventory_item_code farms.farm_zfmv_fair_market_values.inventory_item_code%type,
   in in_file_location farms.farm_zfmv_fair_market_values.file_location%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_zfmv_fair_market_values (
        line_number,
        program_year,
        period,
        average_price,
        percent_variance,
        municipality_code,
        crop_unit_code,
        inventory_item_code,
        file_location,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        in_line_number,
        in_program_year,
        in_period,
        in_average_price,
        in_percent_variance,
        in_municipality_code,
        in_crop_unit_code,
        in_inventory_item_code,
        in_file_location,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
