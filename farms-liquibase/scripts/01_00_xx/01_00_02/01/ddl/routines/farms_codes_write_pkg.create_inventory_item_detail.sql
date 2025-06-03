create or replace procedure farms_codes_write_pkg.create_inventory_item_detail(
   in in_inventory_item_code farms.inventory_item_detail.inventory_item_code%type,
   in in_program_year farms.inventory_item_detail.program_year%type,
   in in_eligibility_indicator farms.inventory_item_detail.eligibility_indicator%type,
   in in_fruit_vegetable_type_code farms.inventory_item_detail.fruit_vegetable_type_code%type,
   in in_line_item farms.inventory_item_detail.line_item%type,
   in in_insurable_value farms.inventory_item_detail.insurable_value%type,
   in in_premium_rate farms.inventory_item_detail.premium_rate%type,
   in in_commodity_type_code farms.inventory_item_detail.commodity_type_code%type,
   in in_user farms.inventory_item_detail.update_user%type
)
language plpgsql
as $$
begin
    insert into farms.inventory_item_detail (
        inventory_item_code,
        program_year,
        eligibility_indicator,
        inventory_item_code,
        create_user,
        create_date,
        update_user,
        update_date,
        fruit_vegetable_type_code,
        line_item,
        insurable_value,
        premium_rate,
        commodity_type_code,
        revision_count
    ) values (
        nextval('farms.seq_iid'),
        in_program_year,
        in_eligibility_indicator,
        in_inventory_item_code,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp,
        in_fruit_vegetable_type_code,
        in_line_item,
        in_insurable_value,
        in_premium_rate,
        in_commodity_type_code,
        1
    );
end;
$$;
