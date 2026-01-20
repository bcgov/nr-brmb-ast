create or replace procedure farms_codes_write_pkg.create_expected_production(
   in in_crop_unit_code farms.farm_expected_productions.crop_unit_code%type,
   in in_inventory_code farms.farm_expected_productions.inventory_item_code%type,
   in in_expected_production_value farms.farm_expected_productions.expected_prodctn_per_prod_unit%type,
   in in_farm_user farms.farm_expected_productions.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_expected_productions (
        expected_production_id,
        expected_prodctn_per_prod_unit,
        inventory_item_code,
        crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_ep_seq'),
        in_expected_production_value,
        in_inventory_code,
        in_crop_unit_code,
        1,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp
    );

end;
$$;
