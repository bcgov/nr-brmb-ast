create or replace procedure farms_codes_write_pkg.expire_fmv(
   in in_fair_market_value_id farms.fair_market_value.fair_market_value_id%type,
   in in_program_year farms.fair_market_value.program_year%type,
   in in_period farms.fair_market_value.period%type,
   in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
   in in_municipality_code farms.fair_market_value.municipality_code%type,
   in in_revision_count farms.fair_market_value.revision_count%type,
   in in_user farms.fair_market_value.update_user%type
)
language plpgsql
as $$
declare
    v_default_crop_unit_code farms.crop_unit_default.crop_unit_code%type;
begin

    if in_fair_market_value_id is not null then
        update farms.fair_market_value fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmr.update_user = in_user,
            fmv.update_date = current_timestamp
        where fmv.fair_market_value_id = in_fair_market_value_id
        and fmv.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

    select cud.crop_unit_code
    into v_default_crop_unit_code
    from farms.crop_unit_default cud
    where cud.inventory_item_code = in_inventory_item_code;

    if v_default_crop_unit_code is not null then
        update farms.fair_market_value fmv
        set fmv.expiry_date = current_date,
            fmv.revision_count = fmv.revision_count + 1,
            fmv.update_user = in_user,
            fmv.update_date = current_timestamp
        where fmv.program_year = in_program_year
        and fmv.period = in_period
        and fmv.inventory_item_code = in_inventory_item_code
        and fmv.municipality_code = in_municipality_code
        and (fmv.expiry_date is null or fmv.expiry_date > current_date)
        and (in_fair_market_value_id is null or fmv.fair_market_value_id != in_fair_market_value_id);
    end if;

end;
$$;
