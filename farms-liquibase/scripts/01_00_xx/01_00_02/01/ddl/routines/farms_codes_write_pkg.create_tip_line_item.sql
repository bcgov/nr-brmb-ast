create or replace procedure farms_codes_write_pkg.create_tip_line_item(
   in in_line_item farms.tip_line_item.line_item%type,
   in in_operating_cost_indicator farms.tip_line_item.operating_cost_indicator%type,
   in in_direct_expense_indicator farms.tip_line_item.direct_expense_indicator%type,
   in in_machinery_expense_indicator farms.tip_line_item.machinery_expense_indicator%type,
   in in_land_and_building_expense_indicator farms.tip_line_item.land_and_building_expense_indicator%type,
   in in_overhead_expense_indicator farms.tip_line_item.overhead_expense_indicator%type,
   in in_program_payment_for_tips_indicator farms.tip_line_item.program_payment_for_tips_indicator%type,
   in in_other_indicator farms.tip_line_item.other_indicator%type,
   in in_tip_farm_type_1_lookup_id farms.tip_line_item.tip_farm_type_1_lookup_id%type,
   in in_farm_user farms.tip_line_item.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.tip_line_item (
        tip_line_item_id,
        line_item,
        operating_cost_indicator,
        direct_expense_indicator,
        machinery_expense_indicator,
        land_and_building_expense_indicator,
        overhead_expense_indicator,
        program_payment_for_tips_indicator,
        other_indicator,
        tip_farm_type_1_lookup_id,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        nextval('farms.seq_tli'),
        in_line_item,
        in_operating_cost_indicator,
        in_direct_expense_indicator,
        in_machinery_expense_indicator,
        in_land_and_building_expense_indicator,
        in_overhead_expense_indicator,
        in_program_payment_for_tips_indicator,
        in_other_indicator,
        in_tip_farm_type_1_lookup_id,
        1,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp
    );
end;
$$;
