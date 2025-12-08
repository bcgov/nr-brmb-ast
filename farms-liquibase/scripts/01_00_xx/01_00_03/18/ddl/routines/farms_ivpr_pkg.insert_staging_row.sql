drop procedure if exists farms_ivpr_pkg.insert_staging_row(
    bigint,
    smallint,
    varchar,
    numeric,
    numeric,
    varchar
);

create or replace procedure farms_ivpr_pkg.insert_staging_row(
   in in_line_number farms.farm_zivpr_iv_premium_rates.line_number%type,
   in in_program_year farms.farm_zivpr_iv_premium_rates.program_year%type,
   in in_inventory_item_code farms.farm_zivpr_iv_premium_rates.inventory_item_code%type,
   in in_insurable_value farms.farm_zivpr_iv_premium_rates.insurable_value%type,
   in in_premium_rate farms.farm_zivpr_iv_premium_rates.premium_rate%type,
   in in_file_location farms.farm_zivpr_iv_premium_rates.file_location%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_zivpr_iv_premium_rates (
        line_number,
        program_year,
        inventory_item_code,
        insurable_value,
        premium_rate,
        file_location,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        in_line_number,
        in_program_year,
        in_inventory_item_code,
        in_insurable_value,
        in_premium_rate,
        in_file_location,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
