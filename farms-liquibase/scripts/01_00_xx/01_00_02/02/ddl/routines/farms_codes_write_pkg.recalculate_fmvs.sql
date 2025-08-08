create or replace procedure farms_codes_write_pkg.recalculate_fmvs(
   in in_program_year farms.farm_fair_market_values.program_year%type,
   in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
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
        -- Expire FMVs for non-default crop units
        update farms.farm_fair_market_values fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmv.who_updated = in_user,
            fmv.when_updated = current_timestamp
        where fmv.program_year = in_program_year
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.crop_unit_code != v_default_crop_unit_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date);

        -- Create non-default crop unit FMVs using conversion factors
        insert into farms.farm_fair_market_values (
            fair_market_value_id,
            program_year,
            period,
            average_price,
            percent_variance,
            expiry_date,
            inventory_item_code,
            municipality_code,
            crop_unit_code,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        )
        select nextval('farms.farm_fmv_seq'),
               fmv.program_year,
               fmv.period,
               fmv.average_price / cucf.conversion_factor,
               fmv.percent_variance,
               null,
               fmv.inventory_item_code,
               fmv.municipality_code,
               cucf.target_crop_unit_code,
               1,
               in_user,
               current_timestamp,
               in_user,
               current_timestamp
        from farms.farm_fair_market_values fmv
        join farms.farm_crop_unit_conversn_fctrs cucf on cucf.inventory_item_code = fmv.inventory_item_code
        where fmv.program_year = in_program_year
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.crop_unit_code = v_default_crop_unit_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date);

    end if;

end;
$$;
