create or replace procedure farms_codes_write_pkg.create_inventory_item_code(
   in in_inventory_item_code farms.inventory_item_code.inventory_item_code%type,
   in in_description farms.inventory_item_code.description%type,
   in in_rollup_inventory_item_code farms.inventory_item_attribute.rollup_inventory_item_code%type,
   in in_effective_date farms.inventory_item_code.effective_date%type,
   in in_expiry_date farms.inventory_item_code.expiry_date%type,
   in in_user farms.inventory_item_code.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.inventory_item_code (
        inventory_item_code,
        description,
        effective_date,
        expiry_date,
        create_user,
        create_date,
        update_user,
        update_date,
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

    insert into farms.inventory_item_attribute (
        inventory_item_attribute_id,
        inventory_item_code,
        rollup_inventory_item_code,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        nextval('farms.seq_iia'),
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
