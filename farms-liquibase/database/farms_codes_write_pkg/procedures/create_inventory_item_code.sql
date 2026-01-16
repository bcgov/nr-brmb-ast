create or replace procedure farms_codes_write_pkg.create_inventory_item_code(
   in in_inventory_item_code farms.farm_inventory_item_codes.inventory_item_code%type,
   in in_description farms.farm_inventory_item_codes.description%type,
   in in_rollup_inventory_item_code farms.farm_inventory_item_attributes.rollup_inventory_item_code%type,
   in in_effective_date farms.farm_inventory_item_codes.established_date%type,
   in in_expiry_date farms.farm_inventory_item_codes.expiry_date%type,
   in in_user farms.farm_inventory_item_codes.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_inventory_item_codes (
        inventory_item_code,
        description,
        established_date,
        expiry_date,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        in_inventory_item_code,
        in_description,
        in_effective_date,
        in_expiry_date,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );

    insert into farms.farm_inventory_item_attributes (
        inventory_item_attrib_id,
        inventory_item_code,
        rollup_inventory_item_code,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        nextval('farms.farm_iia_seq'),
        in_inventory_item_code,
        in_rollup_inventory_item_code,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        1
    );
end;
$$;
