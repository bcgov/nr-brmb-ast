create or replace procedure farms_codes_write_pkg.update_tip_line_item(
   in in_tip_line_item_id farms.tip_line_item.tip_line_item_id%type,
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

    update farms.tip_line_item tli
    set tli.line_item = in_line_item,
        tli.operating_cost_indicator = in_operating_cost_indicator,
        tli.direct_expense_indicator = in_direct_expense_indicator,
        tli.machinery_expense_indicator = in_machinery_expense_indicator,
        tli.land_and_building_expense_indicator = in_land_and_building_expense_indicator,
        tli.overhead_expense_indicator = in_overhead_expense_indicator,
        tli.program_payment_for_tips_indicator = in_program_payment_for_tips_indicator,
        tli.other_indicator = in_other_indicator,
        tli.tip_farm_type_1_lookup_id = in_tip_farm_type_1_lookup_id,
        tli.revision_count = tli.revision_count + 1,
        tli.update_user = in_farm_user
    where tli.tip_line_item_id = in_tip_line_item_id;

end;
$$;
