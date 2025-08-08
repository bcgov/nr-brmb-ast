create or replace procedure farms_codes_write_pkg.expire_fmv(
   in in_fair_market_value_id farms.farm_fair_market_values.fair_market_value_id%type,
   in in_program_year farms.farm_fair_market_values.program_year%type,
   in in_period farms.farm_fair_market_values.period%type,
   in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type,
   in in_municipality_code farms.farm_fair_market_values.municipality_code%type,
   in in_revision_count farms.farm_fair_market_values.revision_count%type,
   in in_user farms.farm_fair_market_values.who_updated%type
)
language plpgsql
as $$
declare
    v_default_crop_unit_code farms.farm_crop_unit_defaults.crop_unit_code%type;
begin

    if in_fair_market_value_id is not null then
        update farms.farm_fair_market_values fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmr.who_updated = in_user,
            fmv.when_updated = current_timestamp
        where fmv.fair_market_value_id = in_fair_market_value_id
        and fmv.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

    select cud.crop_unit_code
    into v_default_crop_unit_code
    from farms.farm_crop_unit_defaults cud
    where cud.inventory_item_code = in_inventory_item_code;

    if v_default_crop_unit_code is not null then
        update farms.farm_fair_market_values fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmv.who_updated = in_user,
            fmv.when_updated = current_timestamp
        where fmv.program_year = in_program_year
        and fmv.period = in_period
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.municipality_code = in_municipality_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date)
        and (in_fair_market_value_id is null or fmv.fair_market_value_id != in_fair_market_value_id);
    end if;

end;
$$;
