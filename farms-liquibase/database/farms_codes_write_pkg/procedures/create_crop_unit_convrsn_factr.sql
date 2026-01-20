create or replace procedure farms_codes_write_pkg.create_crop_unit_convrsn_factr(
   in in_inventory_item_code farms.farm_crop_unit_conversn_fctrs.inventory_item_code%type,
   in in_target_crop_unit_code farms.farm_crop_unit_conversn_fctrs.target_crop_unit_code%type,
   in in_conversion_factor farms.farm_crop_unit_conversn_fctrs.conversion_factor%type,
   in in_user farms.farm_crop_unit_conversn_fctrs.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_crop_unit_conversn_fctrs (
        crop_unit_conversion_factor_id,
        conversion_factor,
        inventory_item_code,
        target_crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_cucf_seq'),
        in_conversion_factor,
        in_inventory_item_code,
        in_target_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
