create or replace procedure farms_codes_write_pkg.create_fmv(
   in in_program_year farms.fair_market_value.program_year%type,
   in in_period farms.fair_market_value.period%type,
   in in_average_price farms.fair_market_value.average_price%type,
   in in_percent_variance farms.fair_market_value.percent_variance%type,
   in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
   in in_municipality_code farms.fair_market_value.municipality_code%type,
   in in_crop_unit_code farms.fair_market_value.crop_unit_code%type,
   in in_user farms.fair_market_value.update_user%type
)
language plpgsql
as $$
declare
    v_default_crop_unit_code farms.crop_unit_default.crop_unit_code%type;
begin

    select cud.crop_unit_code
    into v_default_crop_unit_code
    from farms.crop_unit_default cud
    where cud.inventory_item_code = in_inventory_item_code;

    if v_default_crop_unit_code is not null then
        call farms_codes_write_pkg.expire_fmv(
            null,
            in_program_year,
            in_period,
            in_inventory_item_code,
            in_municipality_code,
            null,
            in_user
        );
    end if;

    insert into farms.fair_market_value (
        fair_market_value_id,
        program_year,
        period,
        average_price,
        percent_variance,
        inventory_item_code,
        municipality_code,
        crop_unit_code,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        nextval('farms.seq_fmv'),
        in_program_year,
        in_period,
        in_average_price,
        in_percent_variance,
        in_inventory_item_code,
        in_municipality_code,
        in_crop_unit_code,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );

    if v_default_crop_unit_code is not null then
        insert into farms.fair_market_value (
            fair_market_value_id,
            program_year,
            period,
            average_price,
            percent_variance,
            inventory_item_code,
            municipality_code,
            crop_unit_code,
            create_user,
            create_date,
            update_user,
            update_date,
            revision_count
        )
        select nextval('farms.seq_fmv'),
               in_program_year,
               in_period,
               in_average_price / cucf.conversion_factor average_price,
               in_percent_variance,
               in_inventory_item_code,
               in_municipality_code,
               cucf.target_crop_unit_code,
               in_user,
               current_timestamp,
               in_user,
               current_timestamp,
               1
        from farms.crop_unit_conversion_factor cucf
        where cucf.inventory_item_code = in_inventory_item_code;
    end if;

end;
$$;
