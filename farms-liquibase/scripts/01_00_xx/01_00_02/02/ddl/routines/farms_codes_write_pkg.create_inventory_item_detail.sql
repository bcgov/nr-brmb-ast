create or replace procedure farms_codes_write_pkg.create_inventory_item_detail(
   in in_inventory_item_code farms.farm_inventory_item_details.inventory_item_code%type,
   in in_program_year farms.farm_inventory_item_details.program_year%type,
   in in_eligibility_indicator farms.farm_inventory_item_details.eligibility_ind%type,
   in in_fruit_vegetable_type_code farms.farm_inventory_item_details.fruit_veg_type_code%type,
   in in_line_item farms.farm_inventory_item_details.line_item%type,
   in in_insurable_value farms.farm_inventory_item_details.insurable_value%type,
   in in_premium_rate farms.farm_inventory_item_details.premium_rate%type,
   in in_commodity_type_code farms.farm_inventory_item_details.commodity_type_code%type,
   in in_user farms.farm_inventory_item_details.who_updated%type
)
language plpgsql
as $$
begin
    insert into farms.farm_inventory_item_details (
        inventory_item_code,
        program_year,
        eligibility_ind,
        inventory_item_code,
        who_created,
        when_created,
        who_updated,
        when_updated,
        fruit_veg_type_code,
        line_item,
        insurable_value,
        premium_rate,
        commodity_type_code,
        revision_count
    ) values (
        nextval('farms.farm_iid_seq'),
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
