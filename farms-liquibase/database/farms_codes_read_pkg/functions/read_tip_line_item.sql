create or replace function farms_codes_read_pkg.read_tip_line_item(
    in in_line_item_id farms.farm_tip_line_items.tip_line_item_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select tli.tip_line_item_id,
               tli.line_item,
               tli.operating_cost_ind,
               tli.direct_expense_ind,
               tli.machinery_expense_ind,
               tli.land_and_building_expense_ind,
               tli.overhead_expense_ind,
               tli.program_payment_for_tips_ind,
               tli.other_ind,
               tli.tip_farm_type_1_lookup_id,
               tft1.farm_type_1_name
        from farms.farm_tip_line_items tli
        left outer join farms.farm_tip_farm_type_1_lookups tft1 on tli.tip_farm_type_1_lookup_id = tft1.tip_farm_type_1_lookup_id
        where tli.tip_line_item_id = in_line_item_id;
    return cur;

end;
$$;
