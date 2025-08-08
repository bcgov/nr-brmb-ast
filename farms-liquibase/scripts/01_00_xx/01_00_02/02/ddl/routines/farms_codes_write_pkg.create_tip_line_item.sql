create or replace procedure farms_codes_write_pkg.create_tip_line_item(
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

    insert into farms.farm_tip_line_items (
        tip_line_item_id,
        line_item,
        operating_cost_ind,
        direct_expense_ind,
        machinery_expense_ind,
        land_and_building_expense_ind,
        overhead_expense_ind,
        program_payment_for_tips_ind,
        other_ind,
        tip_farm_type_1_lookup_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_tli_seq'),
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
