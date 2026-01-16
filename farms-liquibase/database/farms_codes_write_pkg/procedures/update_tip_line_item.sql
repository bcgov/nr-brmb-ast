create or replace procedure farms_codes_write_pkg.update_tip_line_item(
   in in_tip_line_item_id farms.farm_tip_line_items.tip_line_item_id%type,
   in in_line_item farms.farm_tip_line_items.line_item%type,
   in in_operating_cost_indicator farms.farm_tip_line_items.operating_cost_ind%type,
   in in_direct_expense_indicator farms.farm_tip_line_items.direct_expense_ind%type,
   in in_machinery_expense_indicator farms.farm_tip_line_items.machinery_expense_ind%type,
   in in_land_and_building_expense_indicator farms.farm_tip_line_items.land_and_building_expense_ind%type,
   in in_overhead_expense_indicator farms.farm_tip_line_items.overhead_expense_ind%type,
   in in_program_payment_for_tips_indicator farms.farm_tip_line_items.program_payment_for_tips_ind%type,
   in in_other_indicator farms.farm_tip_line_items.other_ind%type,
   in in_tip_farm_type_1_lookup_id farms.farm_tip_line_items.tip_farm_type_1_lookup_id%type,
   in in_farm_user farms.farm_tip_line_items.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_tip_line_items
    set line_item = in_line_item,
        operating_cost_ind = in_operating_cost_indicator,
        direct_expense_ind = in_direct_expense_indicator,
        machinery_expense_ind = in_machinery_expense_indicator,
        land_and_building_expense_ind = in_land_and_building_expense_indicator,
        overhead_expense_ind = in_overhead_expense_indicator,
        program_payment_for_tips_ind = in_program_payment_for_tips_indicator,
        other_ind = in_other_indicator,
        tip_farm_type_1_lookup_id = in_tip_farm_type_1_lookup_id,
        revision_count = revision_count + 1,
        who_updated = in_farm_user
    where tip_line_item_id = in_tip_line_item_id;

end;
$$;
