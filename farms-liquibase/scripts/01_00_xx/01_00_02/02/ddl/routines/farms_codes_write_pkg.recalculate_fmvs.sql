create or replace procedure farms_codes_write_pkg.recalculate_fmvs(
   in in_program_year farms.fair_market_value.program_year%type,
   in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
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
        -- Expire FMVs for non-default crop units
        update farms.fair_market_value fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmv.update_user = in_user,
            fmv.update_date = current_timestamp
        where fmv.program_year = in_program_year
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.crop_unit_code != v_default_crop_unit_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date);

        -- Create non-default crop unit FMVs using conversion factors
        insert into farms.fair_market_value (
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
            create_user,
            create_date,
            update_user,
            update_date
        )
        select nextval('farms.seq_fmv'),
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
        from farms.fair_market_value fmv
        join farms.crop_unit_conversion_factor cucf on cucf.inventory_item_code = fmv.inventory_item_code
        where fmv.program_year = in_program_year
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.crop_unit_code = v_default_crop_unit_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date);

    end if;

end;
$$;
