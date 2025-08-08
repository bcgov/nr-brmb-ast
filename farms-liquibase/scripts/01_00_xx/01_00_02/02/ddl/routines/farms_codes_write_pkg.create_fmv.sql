create or replace procedure farms_codes_write_pkg.create_fmv(
   in in_program_year farms.farm_fair_market_values.program_year%type,
   in in_period farms.farm_fair_market_values.period%type,
   in in_average_price farms.farm_fair_market_values.average_price%type,
   in in_percent_variance farms.farm_fair_market_values.percent_variance%type,
   in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
   in in_municipality_code farms.farm_fair_market_values.municipality_code%type,
   in in_crop_unit_code farms.farm_fair_market_values.crop_unit_code%type,
   in in_user farms.farm_fair_market_values.who_updated%type
)
language plpgsql
as $$
declare
    v_default_crop_unit_code farms.farm_crop_unit_defaults.crop_unit_code%type;
begin

    select cud.crop_unit_code
    into v_default_crop_unit_code
    from farms.farm_crop_unit_defaults cud
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

    insert into farms.farm_fair_market_values (
        fair_market_value_id,
        program_year,
        period,
        average_price,
        percent_variance,
        inventory_item_code,
        municipality_code,
        crop_unit_code,
        who_created,
        when_created,
        who_updated,
        when_updated,
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
        insert into farms.farm_fair_market_values (
            fair_market_value_id,
            program_year,
            period,
            average_price,
            percent_variance,
            inventory_item_code,
            municipality_code,
            crop_unit_code,
            who_created,
            when_created,
            who_updated,
            when_updated,
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
        from farms.farm_crop_unit_conversn_fctrs cucf
        where cucf.inventory_item_code = in_inventory_item_code;
    end if;

end;
$$;
